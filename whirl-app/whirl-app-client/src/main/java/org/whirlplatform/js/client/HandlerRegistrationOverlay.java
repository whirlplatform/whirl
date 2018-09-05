package org.whirlplatform.js.client;

import org.timepedia.exporter.client.ExportOverlay;

//@Export("HandlerRegistration")
//@ExportPackage("Whirl")
public abstract class HandlerRegistrationOverlay implements
        ExportOverlay<HandlerRegistrationWrapper> {

    public abstract void removeHandler();

}
