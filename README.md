[![MIT License](https://img.shields.io/badge/License-MIT-green.svg)](https://choosealicense.com/licenses/mit/)
![Static Badge](https://img.shields.io/badge/Language-Java_21-orange)
[![Maven Central](https://img.shields.io/maven-central/v/io.github.theunknownsoul/java-cli-spinner.svg?label=Maven%20Central)](https://search.maven.org/artifact/io.github.theunknownsoul/java-cli-spinner)


# Java command line spinner

🤝 Small utility for Java developers who creates command line tools.

![Screenshot](/screenshot.gif)
## Installation

Install spinner with importing from maven central:

```bash
<dependency>
  <groupId>io.github.theunknownsoul</groupId>
  <artifactId>java-cli-spinner</artifactId>
  <version>0.1.1</version>
</dependency>
```

## Usage

```java
import spinner.implementation.Spinner;
import spinner.implementation.SpinnerStyle;
import spinner.implementation.SpinnerStylesLoader;
import java.nio.file.*;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class ReadFileExample {
    public static void main(String[] args) {
        try {
            SpinnerStylesLoader loader = SpinnerStylesLoader.fromClasspath("spinners.json");
            // change type of styling. see spinners.json for more in resources
            SpinnerStyle style = loader.get("dots2");
            try (Spinner s = Spinner.start(style, "Reading file.txt…")) {
                List<String> lines = Files.readAllLines(Path.of("file.txt"), StandardCharsets.UTF_8);
                s.succeed("Loaded " + lines.size() + " lines");
        } catch (IOException e) {
                s.fail("Failed: " + e.getMessage());
            }
            
        }
    }
}
```

## License
This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Acknowledgments
This project was inspired by [cli project on javascript](https://github.com/sindresorhus/cli-spinners)
and other spinners implementations in different programming languages.
