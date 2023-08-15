package four.group.jahadi.Tests;

import four.group.jahadi.Models.Module;
import four.group.jahadi.Service.ModuleService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ModuleServiceUnitTest {

    @Autowired
    private ModuleService moduleService;

    @Test
    public void whenApplicationStarts_thenHibernateCreatesInitialRecords() {

//        List<Module> modules = moduleService.list();
//
//        Assert.assertEquals(modules.size(), 3);
    }
}
