package controllers.tools;

import javafx.beans.binding.StringBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.util.ResourceBundle;

public class ObservableResourceFactory {
    private final ObjectProperty<ResourceBundle> resources = new SimpleObjectProperty<>();

    public ResourceBundle getResources() {
        return resources.get();
    }

    public ObjectProperty<ResourceBundle> resourcesProperty() {
        return resources;
    }

    public void setResources(ResourceBundle resources) {
        this.resources.set(resources);
    }

    /**
     * Binds strings.
     *
     * @param key Key for resource.
     * @return binding string.
     */
    public StringBinding getStringBinding(String key) {
        return new StringBinding() {
            {
                bind(resourcesProperty());
            }
            @Override
            protected String computeValue() {
                return getResources().getString(key);
            }
        };
    }

}
