# ClassRenamer

🛠️ A Java class file renamer for Java decompilers (such as Procyon), make the decompiled code more readable

## Features
- Renaming for classes/fields/methods
- Lightweight design (no external dependencies)
- Command-line interface

## How to build
```bash
# Clone repository and build
git clone https://github.com/SmithGoll/ClassRenamer.git
cd ClassRenamer
./build.sh
```

## How to use
### Method 1: Run from Source
```bash
# Build and execute
./run.sh [in_jar] [out_jar]
```

### Method 2: Use Pre-built Release
```bash
# Download latest JAR from releases
java -jar ClassRenamer-1.0.0.jar [in_jar] [out_jar]
```

## ATTENTION
- **This tool is currently designed for CLASS FILES WITHOUT ANNOTATIONS ONLY**
- Using it on files containing annotations may cause:
  - Incorrect renaming results
  - Processing errors
  - Unexpected crashes
- Future versions will include proper annotation handling

## License
This project is licensed under the [MIT License](LICENSE) - feel free to use, modify, and distribute with proper attribution.
