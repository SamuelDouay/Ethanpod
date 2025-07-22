package fr.github.ethanpod.util.manager;

import java.util.Collection;
import java.util.EnumMap;
import java.util.Map;

public class BaseServiceManager<T> {
    protected final Map<ServiceConstants, T> services = new EnumMap<>(ServiceConstants.class);

    protected void registerService(ServiceConstants serviceConstants, T service) {
        services.put(serviceConstants, service);
    }

    @SuppressWarnings("unchecked")
    protected <S extends T> S getService(ServiceConstants serviceConstants, Class<S> serviceType) {
        T service = services.get(serviceConstants);
        if (service == null) {
            throw new IllegalArgumentException("Service not found: " + serviceConstants.getName());
        }
        if (!serviceType.isInstance(service)) {
            throw new ClassCastException("Service " + serviceConstants.getName() + " is not of type " + serviceType.getName());
        }
        return (S) service;
    }

    protected Collection<T> getAllServices() {
        return services.values();
    }

    protected boolean hasService(ServiceConstants serviceConstants) {
        return services.containsKey(serviceConstants);
    }
}