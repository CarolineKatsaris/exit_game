# Exit-Game

A virus-themed escape game educational project developed in Java.

## Overview

Exit-Game is an interactive puzzle game where players must answer quiz questions to stop a spreading virus. The game features multiple rooms, progressive difficulty levels, and a database-driven question system.

## Project Structure
```

Exit-Game/
├── main/                  # Main game application
│   ├── src/              # Source code (Model, View, Controller)
│   ├── resources/        # Game images and assets
│   ├── db/               # Database files
│   └── test/             # Unit tests
├── examples/             # Example programs and tutorials
├── prototypes/           # Experimental prototypes
├── lib/                  # External libraries (SQLite JDBC driver)
├── out/                  # Build output directory
│   └── artifacts/
│       ├── Exit_Game_jar/    # Game files ready for distribution
│       └── Exit-Game_1_0_<x>.zip  # Packaged game for end users
├── META-INF/             # Java manifest files
└── README.md             # This file
```
## Prerequisites

- **Java 17** (Eclipse Temurin or later)
- **IntelliJ IDEA** (Community or Ultimate Edition)
- **Git**

## Installation & Setup

### 1. Clone the Repository
```
bash
git clone https://gitlab.lrz.de/00000000014C06C6/exit-game.git
cd exit-game
```
### 2. Install Java 17

If Java 17 is not installed:

1. Open IntelliJ IDEA
2. Go to **File → Project Structure** (`Ctrl+Alt+Shift+S`)
3. Under **Project Settings → Project**, click the **SDK** dropdown
4. Select **SDK → Download JDK**
5. Choose:
   - **Version**: 17
   - **Vendor**: Eclipse Temurin
6. Click **Download** and wait for installation to complete
7. Click **OK**

### 3. Open the Project

1. In IntelliJ IDEA, select **File → Open**
2. Navigate to and select the `Exit-Game` folder
3. Click **OK**
4. Wait for IntelliJ to index the project

### 4. Verify Setup

1. Ensure **Java 17** is shown in the bottom-right corner
2. Open `examples/hello-world/src/HelloWorld.java`
3. Right-click and select **Run 'HelloWorld.main()'**
4. Verify "Hello, World!" appears in the console output

## Building the Game for Distribution

IntelliJ IDEA is configured to automatically build the game artifacts when you compile. Follow these steps to create the complete distribution package:

### Method 1: Rebuild Project (Builds Both Artifacts)

Alternatively, you can rebuild the entire project which will build both artifacts automatically:

1. Go to **Build → Rebuild Project**
2. This rebuilds all artifacts including the JAR and support files

### Method 2: Build Artifacts Separately
#### Step 1: Build the JAR File

1. In IntelliJ IDEA, go to **Build → Build Artifacts**
2. Select **Exit-Game:jar → Build**
3. IntelliJ will compile all source code and create `Exit-Game.jar` in `out/artifacts/Exit_Game_jar/`

#### Step 2: Copy Support Files (Important!)

After building the JAR, you **must** copy the support files (database and readme):

1. Go to **Build → Build Artifacts**
2. Select **Copy sqlite and start scripts → Build**
3. This will copy the following files to `out/artifacts/Exit_Game_jar/`:
   - `ExitGame.sqlite` (game database)
   - `README-ExitGame.txt` (end-user instructions)

**⚠️ Important**: Both build steps must be executed. The "Copy sqlite and start scripts" step is essential for the game to run.

### Verifying the Build

After completing both build steps, verify that all files exist in `out/artifacts/Exit_Game_jar/`:
```

out/artifacts/Exit_Game_jar/
├── Exit-Game.jar           ✓ (main game executable)
├── ExitGame.sqlite         ✓ (game database)
└── README-ExitGame.txt     ✓ (end-user instructions)
```
**If any files are missing:**
- Verify you executed both build steps
- Run **Build → Rebuild Project** to rebuild all artifacts
- Check that `.idea/artifacts/Copy_sqlite_and_start_scripts.xml` exists

## Creating the Distribution ZIP

To package the game for end users, you need to create a ZIP file containing all necessary files:

### Manual ZIP Creation

#### Windows

1. Open File Explorer and navigate to `out/artifacts/Exit_Game_jar/`
2. Select all files:
   - Exit-Game.jar
   - ExitGame.sqlite
   - README-ExitGame.txt
3. Right-click the selection
4. Select **Send to → Compressed (zipped) folder**
5. Rename the resulting ZIP file to `Exit-Game_1_0_<x>.zip` (x = increasing build number)
6. Move it to `out/artifacts/`

#### macOS

1. Open Finder and navigate to `out/artifacts/Exit_Game_jar/`
2. Select all files:
   - Exit-Game.jar
   - ExitGame.sqlite
   - README-ExitGame.txt
3. Right-click and select **Compress** (or use `Cmd+C`)
4. The ZIP file will be created as `Archive.zip`
5. Rename it to `Exit-Game_1_0_<x>.zip` (x = increasing build number)
6. Move it to `out/artifacts/`

#### Linux

Open a terminal and run:
```
bash
cd out/artifacts/Exit_Game_jar/
zip -r ../Exit-Game_1_0_<x>.zip Exit-Game.jar ExitGame.sqlite README-ExitGame.txt
```
### Automated ZIP Creation (Optional)

If you have 7-Zip or similar tools, you can automate this process:

**Windows (with 7-Zip):**
```
batch
cd out\artifacts\Exit_Game_jar
7z a ..\Exit-Game_1_0_<x>.zip Exit-Game.jar ExitGame.sqlite README-ExitGame.txt
```
**macOS/Linux (with zip):**
```
bash
cd out/artifacts/Exit_Game_jar
zip -r ../Exit-Game_1_0_<x>.zip Exit-Game.jar ExitGame.sqlite README-ExitGame.txt
```
## Running the Game

### From IntelliJ IDEA (Development)

1. Open `main/src/ExitGame.java`
2. Right-click and select **Run 'ExitGame.main()'**

### From Built JAR (Distribution)

After building both artifacts, you can run the game by double-clicking the JAR file:

**All Platforms (Windows, macOS, Linux):**
1. Navigate to `out/artifacts/Exit_Game_jar/`
2. Double-click `Exit-Game.jar`
3. The game will launch

**Alternative - Command Line (all platforms):**
```
bash
java --enable-native-access=ALL-UNNAMED -jar Exit-Game.jar
```
### From ZIP Distribution

End users should:

1. Extract `Exit-Game_1_0_<x>.zip`
2. A folder named `Exit-Game_1_0_<x>` will be created
3. Double-click `Exit-Game.jar` to launch the game

See `README-ExitGame.txt` for detailed end-user instructions.

## Development Workflow

### Adding New Code

**In the main project:**
```
bash
# Add code to main/src/
# Edit game logic, UI, or model
```
**For prototypes:**
```
bash
mkdir -p prototypes/my-prototype/src
# Add experimental code here
```
**For examples:**
```
bash
mkdir -p examples/my-example/src
# Add example programs here
```
After adding new directories, go to **File → Reload from Disk** in IntelliJ.

### Running Tests

1. Navigate to `main/test/`
2. Right-click on a test file
3. Select **Run 'TestClassName'**

Or run all tests via **Build → Run Tests**

## Database

The game uses SQLite for storing questions and player data.

- **Database file**: `ExitGame.sqlite`
- **Location**: Project root and `out/artifacts/Exit_Game_jar/`
- **Connection string**: `jdbc:sqlite:ExitGame.sqlite`

The database is automatically configured and loaded when the game starts.

## Dependencies

- **Java 17 SDK**
- **SQLite JDBC Driver** (included in `lib/sqlite-jdbc-3.51.0.0.jar`)
- **Swing** (built-in with Java)

## Project Configuration

Key configuration files:

- **Artifact build**: `.idea/artifacts/Exit_Game_jar.xml`
- **Artifact copy**: `.idea/artifacts/Copy_sqlite_and_start_scripts.xml`
- **Module config**: `main/main.iml`
- **Manifest**: `META-INF/MANIFEST.MF`

## Troubleshooting

### Build Issues

**Problem**: "Cannot find symbol" errors during build

**Solution**:
1. Go to **File → Invalidate Caches**
2. Select **Invalidate and Restart**
3. Rebuild the project

**Problem**: JAR file is missing after build

**Solution**:
1. Verify the build configuration: **Build → Build Artifacts → Exit-Game:jar → Build**
2. Check that `out/artifacts/Exit_Game_jar/` exists
3. If files are still missing, run **Build → Rebuild Project**

**Problem**: Support files (database and readme) are missing

**Solution**:
1. Execute the second build step: **Build → Build Artifacts → Copy sqlite and start scripts → Build**
2. Verify files exist in `out/artifacts/Exit_Game_jar/`
3. If still missing, run **Build → Rebuild Project** which builds all artifacts

### Runtime Issues

**Problem**: "Database file not found" error

**Solution**:
1. Ensure `ExitGame.sqlite` is in the same directory as `Exit-Game.jar`
2. Verify the database file exists: `out/artifacts/Exit_Game_jar/ExitGame.sqlite`
3. Rebuild the artifacts: **Build → Build Artifacts → Copy sqlite and start scripts → Build**

**Problem**: Game doesn't start from JAR

**Solution**:
1. Verify Java 17 is installed: `java -version`
2. Check the manifest file contains: `Main-Class: ExitGame`
3. Try running directly: `java --enable-native-access=ALL-UNNAMED -jar Exit-Game.jar`

**Problem**: Double-clicking the JAR doesn't work

**Solution**:
1. Ensure Java 17 is properly installed and associated with .jar files
2. On Windows, right-click the JAR and select "Open with" → "Java(TM) Platform SE binary"
3. Use the command line method: `java --enable-native-access=ALL-UNNAMED -jar Exit-Game.jar`

## Collaboration

### Workflow

1. Create a feature branch: `git checkout -b feature/my-feature`
2. Make changes and commit: `git commit -m "Add feature description"`
3. Push: `git push origin feature/my-feature`
4. Create a Merge Request on GitLab

### Code Review

Before merging:
- Ensure the build succeeds: **Build → Rebuild Project**
- Run tests: **Build → Run Tests**
- Verify the JAR and support files are created: **Build → Build Artifacts**

## Releasing a New Version

1. Build the project: **Build → Rebuild Project**
2. Verify all files exist in `out/artifacts/Exit_Game_jar/`:
   - Exit-Game.jar
   - ExitGame.sqlite
   - README-ExitGame.txt
3. Create the ZIP file (see "Creating the Distribution ZIP" section)
4. Name it according to the version: `Exit_Game_X_X_X.zip`
5. Upload to the release repository or distribution platform
6. Update version numbers in `README.md` and project files

## System Requirements

- **Java**: 17 or later
- **RAM**: At least 512 MB
- **Disk Space**: At least 100 MB
- **Display**: 1536 × 1024 minimum resolution
- **Operating System**: Windows, macOS, or Linux


## License

GNU General Public License v3.0

Copyright (c) 2024-2026 Exit-Game Contributors

**Contributors:**
- Anja Schiefer
- Andreas Stark
- Caroline Katsaris
- Sandra Sowa
- Svenja Dorsch

**Special thanks to:**
- Philipp Sadlo

This software is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program. If not, see https://www.gnu.org/licenses/

### Summary of GPL v3

- ✅ You can use this software freely for any purpose
- ✅ You can study, modify, and improve the source code
- ✅ You can distribute the software and your modifications
- ❌ If you distribute modified versions, you must also distribute the source code
- ❌ You cannot use this software in proprietary/closed-source projects without sharing your modifications
- ❌ Any commercial use must comply with these terms

For more information, visit: https://www.gnu.org/licenses/gpl-3.0.html

## Questions & Support

- **Issues**: [Create an issue](https://gitlab.lrz.de/00000000014C06C6/exit-game/-/issues)
- **Discussions**: Use the project chat or communication platform
- **Documentation**: See `main/docs/` for additional documentation

---

**Happy coding!** 🎮

