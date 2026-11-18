package uspace.multiComponents;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "classpath:uspace/multiComponents/features",
        glue = "uspace.multiComponents.glue",
        plugin = {
                "pretty",
                "html:target/cucumber.html",
                "json:target/cucumber.json"
        },
        snippets = CucumberOptions.SnippetType.CAMELCASE
)
public class MultiComponentCucumberTest { }
