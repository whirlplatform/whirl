/*
 * Copyright 2015-2016 Red Hat, Inc, and individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.whirlplatform.jsdoc;

import com.google.common.base.Splitter;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import jsinterop.annotations.*;
import org.apache.commons.lang.StringUtils;
import org.whirlplatform.component.client.annotation.EsParam;
import org.whirlplatform.component.client.annotation.EsReturn;

import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.element.*;
import javax.lang.model.util.ElementFilter;
import java.util.*;
import java.util.function.Predicate;

import static com.google.common.base.CaseFormat.LOWER_CAMEL;
import static com.google.common.base.CaseFormat.UPPER_CAMEL;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;
import static java.util.stream.StreamSupport.stream;

@SupportedAnnotationTypes("jsinterop.annotations.JsType")
@SuppressWarnings({"HardCodedStringLiteral", "Guava", "ResultOfMethodCallIgnored", "SpellCheckingInspection",
        "DuplicateStringLiteralInspection"})
public class EsDocProcessor extends AbstractProcessor {

    private static final String PACKAGE_TO_SCAN = "";

    private static final String TEMPLATES = "templates";

    private static final String AUTO = "<auto>";
    private static final String PADDING = "    ";
    private static final String PARAM_TAG = "@param";
    private static final String RETURN_TAG = "@return";
    private static final String TEMPLATE = "EsDoc.ftl";
    private static final String TYPES = "types";

    private final Multimap<String, EsDocProcessor.Type> types;

    public EsDocProcessor() {
        super(EsDocProcessor.class, TEMPLATES);
        types = HashMultimap.create();
    }

    @Override
    protected boolean onProcess(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for (Element element : roundEnv.getElementsAnnotatedWith(JsType.class)) {
            JsType jsType = element.getAnnotation(JsType.class);
            if (jsType.isNative()) {
                continue;
            }

            TypeElement typeElement = (TypeElement) element;
            PackageElement packageElement = elementUtils.getPackageOf(typeElement);
            if (!packageElement.getQualifiedName().toString().startsWith(PACKAGE_TO_SCAN)) {
                continue;
            }

            EsDocProcessor.Type type = new EsDocProcessor.Type(namespace(packageElement, typeElement), typeName(typeElement),
                    comment(typeElement, ""));
            types.put(type.getNamespace(), type);
            debug("Discovered JsType [%s]", type);

            List<? extends Element> elements = typeElement.getEnclosedElements();
            Predicate<Element> jsRelevant = e -> e != null &&
                    e.getAnnotation(JsIgnore.class) == null &&
                    e.getModifiers().contains(Modifier.PUBLIC);

            // Constructor
            ElementFilter.constructorsIn(elements)
                    .stream()
                    .filter(jsRelevant.and(e -> e.getAnnotation(JsConstructor.class) != null))
                    .findFirst()
                    .ifPresent(e -> type.setConstructor(
                            new EsDocProcessor.Constructor(parameters(e), comment(e, PADDING))));

            // Properties - Fields
            ElementFilter.fieldsIn(elements)
                    .stream()
                    .filter(jsRelevant)
                    .forEach(e -> {
                        boolean setter = !e.getModifiers().contains(Modifier.FINAL);
                        type.addProperty(
                                new EsDocProcessor.Property(propertyName(e), comment(e, PADDING), true, setter, _static(e)));
                    });

            // Properties - Methods (only getters are supported)
            ElementFilter.methodsIn(elements)
                    .stream()
                    .filter(jsRelevant.and(e -> e.getAnnotation(JsProperty.class) != null))
                    .forEach(e -> type.addProperty(
                            new Property(propertyName(e), comment(e, PADDING), true, false, _static(e))));

            // Methods
            ElementFilter.methodsIn(elements)
                    .stream()
                    .filter(jsRelevant.and(e -> e.getAnnotation(JsProperty.class) == null))
                    .forEach(e -> type.addMethod(
                            new EsDocProcessor.Method(methodName(e), parameters(e), comment(e, PADDING), _static(e))));
        }

        if (!types.isEmpty()) {
            types.asMap().forEach((namespace, nsTypes) -> {
                debug("Generating documentation for %s.es6", namespace);
                resource(TEMPLATE, namespace, namespace + ".es6",
                        () -> {
                            Map<String, Object> context = new HashMap<>();
                            context.put(TYPES, nsTypes);
                            return context;
                        });
            });

            info("Successfully generated ES6 documentation.");
            types.clear();
        }
        return false;
    }

    private String namespace(PackageElement packageElement, TypeElement typeElement) {
        JsPackage jsPackage = packageElement.getAnnotation(JsPackage.class);
        JsType jsType = typeElement.getAnnotation(JsType.class);

        String namespace;
        if (jsPackage != null) {
            namespace = jsPackage.namespace();
        } else {
            namespace = AUTO.equals(jsType.namespace())
                    ? packageElement.getQualifiedName().toString()
                    : jsType.namespace();
        }
        return namespace;
    }

    private String comment(Element element, String padding) {
        List<String> parameters = new ArrayList<>();
        String comment = elementUtils.getDocComment(element);
        if (comment != null) {

            // process comment line by line
            List<String> lines = stream(Splitter.on('\n').trimResults().split(comment).spliterator(), false)

                    // not supported by ESDoc
                    .filter(line -> !(line.contains("@author") || line.contains("@version")))

                    // process @param and @return in methods
                    .map(line -> {
                        String result = line;
                        if (element instanceof ExecutableElement) {
                            ExecutableElement method = (ExecutableElement) element;

                            if (line.startsWith(PARAM_TAG)) {
                                String paramType;
                                String lineWithoutParam = line.substring(PARAM_TAG.length());
                                VariableElement parameter = getParameter(method, parameters.size());
                                if (parameter != null) {
                                    EsParam esParam = parameter.getAnnotation(EsParam.class);
                                    if (esParam != null) {
                                        paramType = esParam.value();
                                    } else {
                                        paramType = simpleName(parameter.asType().toString());
                                    }
                                    result = PARAM_TAG + " {" + paramType + "}" + lineWithoutParam;
                                }
                                parameters.add(line); // parameters++

                            } else if (line.startsWith(RETURN_TAG)) {
                                String returnType;
                                EsReturn esReturn = method.getAnnotation(EsReturn.class);
                                if (esReturn != null) {
                                    returnType = esReturn.value();
                                } else {
                                    returnType = simpleName(method.getReturnType().toString());
                                }
                                result = RETURN_TAG + " {" + returnType + "}" + line.substring(RETURN_TAG.length());
                            }
                        }
                        return result;
                    })

                    // format comment and collect into list
                    .map(line -> padding + " * " + line)
                    .collect(toList());

            // remove trailing empty lines
            List<String> reversed = Lists.reverse(lines);
            for (Iterator<String> iterator = reversed.iterator(); iterator.hasNext(); ) {
                String line = iterator.next();
                if (line.equals(padding + " * ")) {
                    iterator.remove();
                } else {
                    break;
                }
            }

            if (reversed.isEmpty()) {
                comment = null;
            } else {
                // add first and last lines
                comment = Lists.reverse(reversed).stream().collect(joining("\n"));
                comment = "/**\n" + comment + "\n" + padding + " */";
            }
        }
        return comment;
    }

    private VariableElement getParameter(ExecutableElement method, int index) {
        List<? extends VariableElement> parameters = method.getParameters();
        return index < parameters.size() ? parameters.get(index) : null;
    }

    private String simpleName(String type) {
        String simple = type.contains(".") ? StringUtils.substringAfterLast(type, ".") : type;
        switch (simple) {
            case "double":
            case "Double":
            case "float":
            case "Float":
            case "int":
            case "Integer":
            case "long":
            case "Long":
            case "Number":
                simple = "number";
                break;

            case "String":
                simple = "string";
                break;
            default:
                break;
        }
        return simple;
    }

    private String typeName(Element element) {
        JsType annotation = element.getAnnotation(JsType.class);
        if (annotation != null) {
            return AUTO.equals(annotation.name()) ? element.getSimpleName().toString() : annotation.name();
        } else {
            return element.getSimpleName().toString();
        }
    }

    private String propertyName(Element element) {
        JsProperty annotation = element.getAnnotation(JsProperty.class);
        if (annotation != null) {
            return AUTO.equals(annotation.name()) ? asProperty(element) : annotation.name();
        } else {
            return asProperty(element);
        }
    }

    private String asProperty(Element method) {
        String simpleName = method.getSimpleName().toString();
        if (simpleName.startsWith("get") || simpleName.startsWith("set")) {
            simpleName = UPPER_CAMEL.to(LOWER_CAMEL, simpleName.substring(3));
        } else if (simpleName.startsWith("is")) {
            simpleName = UPPER_CAMEL.to(LOWER_CAMEL, simpleName.substring(2));
        }
        return simpleName;
    }

    private String methodName(Element element) {
        JsMethod annotation = element.getAnnotation(JsMethod.class);
        if (annotation != null) {
            return AUTO.equals(annotation.name()) ? element.getSimpleName().toString() : annotation.name();
        } else {
            return element.getSimpleName().toString();
        }
    }

    private String parameters(ExecutableElement element) {
        return element.getParameters()
                .stream()
                .map(variable -> variable.getSimpleName().toString())
                .collect(joining(", "));

    }

    private boolean _static(Element element) {
        return element.getModifiers().contains(Modifier.STATIC);
    }


    public static class Type {

        private final String namespace;
        private final String name;
        private final String comment;
        private final List<EsDocProcessor.Property> properties;
        private final List<EsDocProcessor.Method> methods;
        private EsDocProcessor.Constructor constructor;

        Type(String namespace, String name, String comment) {
            this.namespace = namespace;
            this.name = name;
            this.comment = comment;
            this.properties = new ArrayList<>();
            this.methods = new ArrayList<>();
        }

        @Override
        public String toString() {
            return String.format("%s.%s", namespace, name);
        }

        void addProperty(EsDocProcessor.Property property) {
            properties.add(property);
        }

        void addMethod(EsDocProcessor.Method method) {
            methods.add(method);
        }

        public String getNamespace() {
            return namespace;
        }

        public String getName() {
            return name;
        }

        public String getComment() {
            return comment;
        }

        public EsDocProcessor.Constructor getConstructor() {
            return constructor;
        }

        public void setConstructor(EsDocProcessor.Constructor constructor) {
            this.constructor = constructor;
        }

        public List<EsDocProcessor.Property> getProperties() {
            return properties;
        }

        public List<EsDocProcessor.Method> getMethods() {
            return methods;
        }
    }


    public static class Constructor {

        private final String parameters;
        private final String comment;

        Constructor(String parameters, String comment) {
            this.parameters = parameters;
            this.comment = comment;
        }

        @Override
        public String toString() {
            return String.format("(%s)", parameters);
        }

        public String getParameters() {
            return parameters;
        }

        public String getComment() {
            return comment;
        }
    }


    public static class Property {

        private final String name;
        private final String comment;
        private final boolean getter;
        private final boolean setter;
        private final boolean _static;

        Property(String name, String comment, boolean getter, boolean setter,
                 boolean _static) {
            this.name = name;
            this.comment = comment;
            this.getter = getter;
            this.setter = setter;
            this._static = _static;
        }

        @Override
        public String toString() {
            return name;
        }

        public String getName() {
            return name;
        }

        public String getComment() {
            return comment;
        }

        public boolean isGetter() {
            return getter;
        }

        public boolean isSetter() {
            return setter;
        }

        public boolean isStatic() {
            return _static;
        }
    }


    public static class Method {

        private final String name;
        private final String parameters;
        private final String comment;
        private final boolean _static;

        Method(String name, String parameters, String comment, boolean _static) {
            this.name = name;
            this.parameters = parameters;
            this.comment = comment;
            this._static = _static;
        }

        @Override
        public String toString() {
            return String.format("%s(%s)", name, parameters);
        }

        public String getName() {
            return name;
        }

        public String getParameters() {
            return parameters;
        }

        public String getComment() {
            return comment;
        }

        public boolean isStatic() {
            return _static;
        }
    }
}