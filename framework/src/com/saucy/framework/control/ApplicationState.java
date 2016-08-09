package com.saucy.framework.control;

import com.saucy.framework.rendering.Renderable;

public interface ApplicationState extends Renderable {
	void enterState();
	void updateApplication(Application app);
	void exitState();
}