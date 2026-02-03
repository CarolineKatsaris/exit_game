================================================================================
                            EXIT-GAME USER GUIDE
                   Running Exit-Game.jar on Your Computer
================================================================================

⚡ QUICK START (30 SECONDS)
================================================================================

1. Extract Exit-Game_0_1_0.zip (double-click it)
2. Open the Exit-Game folder
3. Double-click Exit-Game.jar
4. Game launches!

If step 3 doesn't work, see "TROUBLESHOOTING" section below.


================================================================================
                            DETAILED INSTRUCTIONS
================================================================================

OVERVIEW
--------
This guide will help you run the Exit-Game application on Windows, Linux,
or macOS. The game files are packaged in a ZIP file for easy distribution.


================================================================================
STEP 1: EXTRACT THE ZIP FILE
================================================================================

WINDOWS
-------

Method 1 (Easiest - Built-in extraction):
    1. Find the Exit-Game_0_1_0.zip file on your computer
    2. Right-click on it
    3. Select "Extract All..." from the menu
    4. A dialog box will appear asking where to extract
    5. Click "Extract"
    6. Windows will create a new folder called "Exit-Game"
    7. The folder will open automatically

Method 2 (Using Windows Explorer):
    1. Find the Exit-Game_0_1_0.zip file
    2. Double-click it
    3. A folder window will open showing the contents
    4. Press Ctrl + A to select all files
    5. Right-click and select "Copy"
    6. Navigate to where you want to save the game
    7. Right-click and select "Paste"


MACOS
-----

Method 1 (Easiest - Automatic):
    1. Find the Exit-Game_0_1_0.zip file on your computer
    2. Double-click on it
    3. macOS automatically extracts it
    4. A new folder "Exit-Game" will appear in the same location

Method 2 (Using Terminal):
    1. Open Terminal (press Command + Space, type "Terminal", press Enter)
    2. Type the following command:
       unzip ~/Downloads/Exit-Game_0_1_0.zip
    3. Press Enter
    4. The files will be extracted


LINUX
-----

Method 1 (Using File Manager):
    1. Open your file manager
    2. Find the Exit-Game_0_1_0.zip file
    3. Right-click on it
    4. Select "Extract Here" or "Extract All"
    5. A new folder "Exit-Game" will be created

Method 2 (Using Terminal):
    1. Open Terminal (press Ctrl + Alt + T)
    2. Navigate to where the ZIP file is located:
       cd ~/Downloads
    3. Type the following command:
       unzip Exit-Game_0_1_0.zip
    4. Press Enter
    5. The files will be extracted


================================================================================
STEP 2: VERIFY THE EXTRACTED FILES
================================================================================

After extraction, you should have a folder named "Exit-Game" containing:

    - Exit-Game.jar         (the main game file)
    - ExitGame.sqlite       (the game database)
    - README-ExitGame.txt   (this file)

If any of these files are missing, the game will not run.
Re-extract the ZIP file and try again.


================================================================================
STEP 3: CHECK JAVA IS INSTALLED
================================================================================

Before running the game, ensure you have Java 17 or later installed.

CHECKING YOUR JAVA VERSION
---------------------------

WINDOWS:
    1. Press Windows Key + R
    2. Type: cmd
    3. Press Enter
    4. In the command window, type:
       java -version
    5. Press Enter
    6. You should see a message showing your Java version

MACOS:
    1. Press Command + Space
    2. Type: Terminal
    3. Press Enter
    4. In the terminal window, type:
       java -version
    5. Press Enter
    6. You should see a message showing your Java version

LINUX:
    1. Press Ctrl + Alt + T to open Terminal
    2. Type:
       java -version
    3. Press Enter
    4. You should see a message showing your Java version

If Java is not installed or you have an older version, download Java 17 from:
Eclipse Temurin (https://adoptium.net/)


================================================================================
STEP 4: RUN THE GAME
================================================================================

EASIEST METHOD - ALL PLATFORMS (Windows, macOS, Linux)
------------------------------------------------------

Simply double-click the Exit-Game.jar file in your Exit-Game folder.
The game should launch automatically.

If double-clicking doesn't work, proceed to the command line method below.


COMMAND LINE METHOD (All Platforms)
-----------------------------------

Windows:
    1. Open the Exit-Game folder
    2. Hold SHIFT and right-click in the empty space of the folder
    3. Select "Open PowerShell window here" or "Open command window here"
    4. Type the following command and press Enter:
       java --enable-native-access=ALL-UNNAMED -jar Exit-Game.jar

macOS/Linux:
    1. Open Terminal

       macOS: Press Command + Space, type "Terminal", press Enter
       Linux: Press Ctrl + Alt + T

    2. Type the following command and press Enter:
       cd path/to/Exit-Game
       (Replace "path/to/Exit-Game" with where you saved the folder)

    3. Type and press Enter:
       java --enable-native-access=ALL-UNNAMED -jar Exit-Game.jar


KEYBOARD SHORTCUT FOR OPENING COMMAND PROMPT
----------------------------------------------

WINDOWS:
    1. Press Windows Key + R at the same time
    2. Type: cmd
    3. Press Enter
    4. Navigate to your Exit-Game folder:
       cd path\to\Exit-Game
    5. Type:
       java --enable-native-access=ALL-UNNAMED -jar Exit-Game.jar

MACOS:
    1. Press Command + Space at the same time
    2. Type: Terminal
    3. Press Enter

LINUX:
    1. Press Ctrl + Alt + T at the same time
    2. Terminal opens automatically


================================================================================
PREREQUISITES
================================================================================

Before you begin, ensure you have Java 17 or later installed on your computer.

DOWNLOADING JAVA (If needed)
----------------------------

If you don't have Java 17 installed, follow these steps:

1. Go to: https://adoptium.net/
2. Click the big blue download button
3. Choose your operating system (Windows, macOS, or Linux)
4. Download the installer
5. Run the installer and follow the instructions
6. Restart your computer after installation
7. Verify installation by checking java -version (see Step 3 above)


================================================================================
TROUBLESHOOTING
================================================================================

PROBLEM: The ZIP file won't extract
--------------------------------------------------------------------------

Windows:
    1. Try right-clicking the ZIP file
    2. Select "Extract All..."
    3. Make sure you have write permissions in that folder
    4. Try extracting to a different location (like Desktop or Documents)

macOS/Linux:
    1. Try double-clicking the ZIP file
    2. If that doesn't work, use Terminal:
       unzip Exit-Game_0_1_0.zip
    3. Check that the file is not corrupted by downloading it again


PROBLEM: "Java not found" or "Java command not recognized"
--------------------------------------------------------------------------

Windows:
    - Download and install Java 17 from https://adoptium.net/
    - Restart your computer after installation
    - Or use the full path to Java, for example:
      C:\Program Files\Eclipse Adoptium\jdk-17.0.x\bin\java.exe
      --enable-native-access=ALL-UNNAMED -jar Exit-Game.jar

macOS:
    - Download and install Java 17 from https://adoptium.net/
    - Or use Homebrew:
      brew install openjdk@17

Linux:
    - Install Java 17 using your package manager

    Ubuntu/Debian:
      sudo apt-get install openjdk-17-jre

    Fedora/RHEL:
      sudo dnf install java-17-openjdk


PROBLEM: "File not found" or "sqlite database error"
--------------------------------------------------------------------------

    - Make sure all files are in the same Exit-Game folder:
      * Exit-Game.jar
      * ExitGame.sqlite

    - Check that filenames are spelled exactly as shown
    - On Linux/macOS, filenames are case-sensitive
    - Try re-extracting the ZIP file


PROBLEM: Game window doesn't appear
--------------------------------------------------------------------------

    - Wait a few seconds—the game may take time to start
    - Check the terminal or command prompt for error messages
    - Try running using the command line method (see Step 4 above)
    - Make sure you have at least 512 MB of free RAM


PROBLEM: I can't find my Exit-Game folder
--------------------------------------------------------------------------

Windows:
    1. Press Windows Key + E to open File Explorer
    2. Look for your Exit-Game folder in Downloads or Desktop
    3. Double-click the folder to open it

macOS:
    1. Click the Finder icon (face icon) in the dock
    2. Look for your Exit-Game folder in Downloads
    3. Double-click the folder to open it

Linux:
    1. Open the File Manager
    2. Look for your Exit-Game folder in your home directory or Downloads
    3. Double-click the folder to open it


PROBLEM: The game runs but has graphics issues
--------------------------------------------------------------------------

    - Try updating your graphics driver
    - Make sure your display supports at least 1536 x 1024 resolution
    - Try running from a terminal to see if there are error messages


PROBLEM: Double-clicking the JAR doesn't work (Windows)
--------------------------------------------------------------------------

    - Right-click the Exit-Game.jar file
    - Select "Open with"
    - Choose "Java(TM) Platform SE binary" or similar
    - If not available, use the command line method instead


================================================================================
SYSTEM REQUIREMENTS
================================================================================

Processor:      Any modern CPU (Intel, AMD, Apple Silicon)
RAM:            At least 512 MB free
Disk Space:     At least 100 MB free
Display:        Minimum 1536 x 1024 resolution recommended
Java:           Java 17 or later
Internet:       Not required (offline play)


================================================================================
NEED HELP?
================================================================================

If you encounter issues:

    1. Make sure the ZIP file was fully extracted
       (All 3 files should be present in the Exit-Game folder)

    2. Check that Java 17 or later is installed
       (Run: java -version in a terminal)

    3. Verify all files are in the same Exit-Game folder:
       - Exit-Game.jar
       - ExitGame.sqlite

    4. Try running from a terminal to see detailed error messages
       (Use the command line method in Step 4)

If problems persist, check the error message displayed and refer to the
troubleshooting section above.

================================================================================
LICENSE
================================================================================

GNU General Public License v3.0

Copyright (c) 2024-2026 Exit-Game Contributors

CONTRIBUTORS:
    - Anja Schiefer
    - Andreas Stark
    - Caroline Staerk
    - Sandra Sowa
    - Svenja Dorsch

SPECIAL THANKS TO:
    - Philipp Sadlo

This software is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program. If not, see https://www.gnu.org/licenses/

WHAT THIS MEANS:
    - You can use this software freely for any purpose
    - You can study, modify, and improve the source code
    - You can distribute the software and your modifications
    - If you distribute modified versions, you must also distribute
      the source code
    - You cannot use this software in proprietary/closed-source projects
      without sharing your modifications
    - Any commercial use must comply with these terms

For more information, visit: https://www.gnu.org/licenses/gpl-3.0.html

================================================================================

Enjoy the game! 🎮

================================================================================