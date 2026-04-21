package Runner;
import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(
        features = "classpath:features",
        glue = {"stepdefinitions", "hooks"},
        plugin = {"pretty", "html:target/report.html"},
        monochrome = true
)
public class TestRunner extends AbstractTestNGCucumberTests{
	
}