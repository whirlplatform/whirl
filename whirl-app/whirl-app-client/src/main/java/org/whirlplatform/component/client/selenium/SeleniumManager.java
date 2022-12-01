package org.whirlplatform.component.client.selenium;
//
//import com.google.gwt.debug.client.DebugInfo;
//import com.google.gwt.dom.client.Document;
//import com.google.gwt.dom.client.Element;
//import com.google.gwt.dom.client.Node;
//
//import BuilderManager;
//import ComponentBuilder;
//
///**
// * 
// * @author semenov_pa
// *
// */
//public class SeleniumManager {
//
//    public static final String LOCATOR_MASK = "whirl://";
//    public static final String LOCATOR_PREFIX_WHIRL_ID = "whirl-id";
//    public static final String LOCATOR_PREFIX_XPATH = "xpath";
//    
//    public static void init() {
//        DebugInfo.setDebugIdAttribute(LOCATOR_PREFIX_WHIRL_ID, false);
//        DebugInfo.setDebugIdPrefix("");
//    }
//
//    
//    /**
//     * По-умолчанию возвращает локатор на оберточный компонент DOM Конкретные
//     * реализации более точно должны реализовать этот метод
//     */
//    public static Locator getLocatorByWhirlIdAndElement(String whirlId, Element wrapper, Element element) {
//        Locator locator = new Locator("dfd");
//        locator.setWhirlId(whirlId);
//        String xPath = SeleniumManager.getXPath(element, wrapper);
//        if(xPath != null){
//            locator.setXPath(xPath);
//        }
//        return locator;
//    }
//
//    
//    /**
//     * по строке вида /div[4]/div/table/tbody/tr[2]/td[2]/div/div/table/tbody/tr/td/div
//     * получить элемент, дочерний для обёрточного элемента текущего билдера 
//     * @param xPath
//     * @return
//     */
//    public static Element locateElementByXPath(Element wrapper, String xPath){
//        Element element = null;
////        if(wrapper == null){
////            throw new Exception("Wrapper is null");
////        }
//        if(wrapper != null){
//            Element current = (Element)wrapper;
//            
//            String[] parts = (xPath != null ? xPath.split("/"): new String[0]);
//            for(String part: parts){
//                String tagName;
//                Integer index;
//                if(!part.isEmpty()){
//    
//                    if(part.contains("[")){
//                        tagName = part.substring(0, part.indexOf("["));
//                        String sIndex = part.substring(part.indexOf("[") + 1, part.indexOf("]"));
//                        index = Integer.parseInt(sIndex) - 1;
//                    } else{
//                        tagName = part;
//                        index = 0;
//                    }
//                    
//                    Element child = getChildByTagName(current, tagName, index);
//                    current = child;
//                    if(current == null){
//                        break;
//                    }
//                }
//            }
//            element = current;
//        }
//        return element;
//    }
//    
//    
//    private static Element getChildByTagName(Element element, String tagName, int index){
//        int c = 0;
//        for(int i = 0; i < element.getChildCount(); i ++){
//            Element child = (Element)element.getChild(i);
//            if(child.hasTagName(tagName) && child.getTagName().equals(tagName)){
//                if(c == index){
//                    return child;
//                }
//                c ++;
//            }
//        }
//        return null;
//    }
//    
//    /**
//     * Возвращает XPath для элемента относительно указанного обёрточного элемента.
//     * @param element элемент
//     * @param wrapper обёрточный элемент
//     * @return
//     */
//    public static String getXPath(Element element, Element wrapper){
//        //если wrapper null, то беру за основу document.body
//        //иначе вычисляю xpath элемента element относительно wrapper
//        
//        Element base = wrapper;
//        StringBuilder xPathBuilder = new StringBuilder();
//        
//        if(wrapper == null){
//            base = Document.get().getBody();
//        }
//        
//        Element current = element;
//        while(current != base && current != null){
//            String part = current.getTagName();
//            Integer childIndex = childIndex(current);
//            if(childIndex == null){
//                return null;
//            }
//            if( childIndex > 0){
//                part = part + "["+childIndex+"]";
//            }
//            xPathBuilder.insert(0, part);
//            xPathBuilder.insert(0, "/");
//            current = current.getParentElement();
//        }
//        
//        return xPathBuilder.toString();
//    }
//    
//    
//    /**
//     * Порядковый номер элемента относительного его родительского элемента
//     * @param element
//     * @return
//     */
//    static Integer childIndex(Element element){
//        Element parent = element.getParentElement();
//        if(parent != null){
//            int counter = 0;
//            for(int i = 0; i < parent.getChildCount(); i ++    ){
//                Node child = parent.getChild(i);
//    //            System.out.println("element tagname: " + element.getTagName()+", child tagname" + ((Element)child).getTagName());
//                if(element.getTagName().equalsIgnoreCase(((Element)child).getTagName())){
//                    counter ++;
//                }
//                if(element == child){
//                    return counter;
//                }
//            }
//        }
//        return null;
//    }
//    
//    
//    /**
//     * Поднимаюсь выше element по DOM и ищу атрибут whirl-id (== AppConstant.WHIRL_ID_ATTRIBUTE_NAME)
//     * Возвращаю значение этого атрибута. 
//     * По нему затем легко будет найти билдер.
//     *   
//     * @param element
//     * @return 
//     */
//    static String findWhirlIdByElement(Element element){
//        Element whirlWrapper = findWhirlWrapperByElement(element);
//        
//        if(whirlWrapper != null){
//            // по этому элементу нахожу билдер
//            return whirlWrapper.getAttribute(SeleniumManager.LOCATOR_PREFIX_WHIRL_ID);
//        }
//        return null;
//    }
//    
//    
//    /**
//     * Для элемента поднимается выше по иерархии элементов HTML 
//     * ищет и возвращает элемент, имеющий атрибут whirl-id.
//     * @param element
//     * @return Либо элемент, имеющий атрибут whirl-id, либо null 
//     */
//    public static Element findWhirlWrapperByElement(Element element){
//        int guard = 1000;
//        Element currentElement = element;
//        while (guard > 0 
//                && currentElement != null 
//                && !currentElement.hasAttribute(SeleniumManager.LOCATOR_PREFIX_WHIRL_ID) ) {
//            currentElement = currentElement.getParentElement();
//            guard--;
//        }
//        if(currentElement != null){
//            // по этому элементу нахожу билдер
//            return currentElement;
//        }
//        return null;
//    }
//    
//    
//
//    
//    /**
//     * Ищет билдер в карте компонентов Whirl по заданному whirlId.
//     * @param whirlId
//     * @return
//     */
//    public static ComponentBuilder findBuilder(String whirlId){
//        ComponentBuilder builder = null;
//        if(whirlId != null){
//            builder = BuilderManager.findBuilder(whirlId, false);
//        }
//        return builder;
//    }
//}
