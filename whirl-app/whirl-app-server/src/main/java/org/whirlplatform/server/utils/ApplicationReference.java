package org.whirlplatform.server.utils;

import org.whirlplatform.meta.shared.editor.ApplicationElement;
import org.whirlplatform.server.compiler.CompilationData;

public class ApplicationReference {

	private ApplicationElement application;
	private CompilationData compilationData;

	public ApplicationReference(ApplicationElement application, CompilationData compilationData) {
		this.application = application;
		this.compilationData = compilationData;
	}

	public ApplicationElement getApplication() {
		return application;
	}

	public CompilationData getCompilationData() {
		return compilationData;
	}
}
