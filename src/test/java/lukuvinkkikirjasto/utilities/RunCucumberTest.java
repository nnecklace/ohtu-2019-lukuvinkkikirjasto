package lukuvinkkikirjasto.utilities;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import io.cucumber.junit.CucumberOptions.SnippetType;
import org.junit.ClassRule;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
    plugin = "pretty", 
    features = "src/test/java/resources", 
    snippets = SnippetType.CAMELCASE 
)

public class RunCucumberTest {
    @ClassRule
    public static ServerRule server = new ServerRule(4567);
}