import com.syl.aclservice.ServiceAclApplication;
import com.syl.aclservice.service.PermissionService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@SpringBootTest(classes = ServiceAclApplication.class)
@RunWith(SpringRunner.class)
public class Test01 {

    @Autowired
    private PermissionService permissionService;

    @Test
    public void test01(){
        List<String> strings = permissionService.selectPermissionValueByUserId("1");
    }

    @Test
    public void test02(){
    }

}
