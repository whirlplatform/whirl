package org.whirlplatform.editor.shared.visitor;

import static org.junit.Assert.assertEquals;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import org.junit.Before;
import org.junit.Test;
import org.whirlplatform.meta.shared.editor.ApplicationElement;
import org.whirlplatform.meta.shared.editor.ComponentElement;
import org.whirlplatform.meta.shared.editor.EventElement;

public class SearchGraphVisitorTest {

    private Set<String> generatedIds = new HashSet<>();

    private ApplicationElement application;

    // Root
    private ComponentElement rootComponent;

    // Root -> First
    private ComponentElement firstComponent;
    // Root -> First -> First
    private ComponentElement firstFirstComponent;
    // Root -> First -> Second
    private ComponentElement firstSecondComponent;

    // Root -> Second
    private ComponentElement secondComponent;

    // Root -> Third
    private ComponentElement thirdComponent;
    // Root -> Third -> First
    private ComponentElement thirdFirstComponent;
    // Root -> Third -> Second
    private ComponentElement thirdSecondComponent;
    // Root -> Third -> Third
    private ComponentElement thirdThirdComponent;

    // Root -> First -> Second -> Event First
    private EventElement rootFirstSecondEFirstEvent;

    // Root -> First -> Second -> Event Second
    private EventElement rootFirstSecondESecondEvent;

    // Root -> First -> Second -> Event Second -> Event First
    private EventElement rootFirstSecondESecondEFirstEvent;

    @Before
    public void setUp() {
        // Application
        application = new ApplicationElement();
        application.setId(generateId());

        // Components
        // Root
        rootComponent = new ComponentElement();
        rootComponent.setId(generateId());
        application.setRootComponent(rootComponent);

        // Root -> First
        firstComponent = new ComponentElement();
        firstComponent.setId(generateId());
        rootComponent.addChild(firstComponent);

        // Root -> First -> First
        firstFirstComponent = new ComponentElement();
        firstFirstComponent.setId(generateId());
        firstComponent.addChild(firstFirstComponent);

        // Root -> First -> Second
        firstSecondComponent = new ComponentElement();
        firstSecondComponent.setId(generateId());
        firstComponent.addChild(firstSecondComponent);

        // Root -> Second
        secondComponent = new ComponentElement();
        secondComponent.setId(generateId());
        rootComponent.addChild(secondComponent);

        // Root -> Third
        thirdComponent = new ComponentElement();
        thirdComponent.setId(generateId());
        rootComponent.addChild(thirdComponent);

        // Root -> Third -> First
        thirdFirstComponent = new ComponentElement();
        thirdFirstComponent.setId(generateId());
        thirdComponent.addChild(thirdFirstComponent);

        // Root -> Third -> Second
        thirdSecondComponent = new ComponentElement();
        thirdSecondComponent.setId(generateId());
        thirdComponent.addChild(thirdSecondComponent);

        // Root -> Third -> Third
        thirdThirdComponent = new ComponentElement();
        thirdThirdComponent.setId(generateId());
        thirdComponent.addChild(thirdThirdComponent);

        // Events
        // Root -> First -> Second -> Event First
        rootFirstSecondEFirstEvent = new EventElement();
        rootFirstSecondEFirstEvent.setId(generateId());
        firstSecondComponent.addEvent(rootFirstSecondEFirstEvent);

        // Root -> First -> Second -> Event Second
        rootFirstSecondESecondEvent = new EventElement();
        rootFirstSecondESecondEvent.setId(generateId());
        firstSecondComponent.addEvent(rootFirstSecondESecondEvent);

        // Root -> First -> Second -> Event Second -> Event First
        rootFirstSecondESecondEFirstEvent = new EventElement();
        rootFirstSecondESecondEFirstEvent.setId(generateId());
        rootFirstSecondESecondEvent.addSubEvent(rootFirstSecondESecondEFirstEvent);

    }

    private String generateId() {
        String result = null;
        boolean next = true;
        while (next) {
            Random random = new Random();
            result = String.valueOf(random.nextInt());
            if (!generatedIds.contains(random)) {
                next = false;
            }
        }
        return result;
    }

    /**
     * Тест SearchElementVisitor
     */
    @Test
    public void searchElementVisitorTest() {
        SearchGraphVisitor visitor = new SearchGraphVisitor();

        ComponentElement findedComponent =
                visitor.search(application, thirdSecondComponent.getId());
        assertEquals("Component not found", thirdSecondComponent, findedComponent);

        EventElement findedEvent =
                visitor.search(application, rootFirstSecondESecondEFirstEvent.getId());
        assertEquals("Event not found", rootFirstSecondESecondEFirstEvent, findedEvent);

    }

}
