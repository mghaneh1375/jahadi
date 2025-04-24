package four.group.jahadi.Service;

import org.springframework.context.ApplicationContext;

public class BeanFetcher {
    private final ApplicationContext applicationContext;

    public BeanFetcher(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public Object getBeanByClass(Class<?> clazz) {
        try {
            return applicationContext.getBean(clazz);
        } catch (Exception e) {
            return null;
        }
    }
}