# World Saver

A mod which creates backups of worlds and stores them in a Google Drive folder.

## ⚒️ Developer Guide

Since World Saver uses a typical Gradle project structure, all you have to do is open the project in any IDE/text editor which supports and once Gradle has finished it's setup, you can run the `build` command. Once building has finished, you can find output JAR in the `build/libs` directory. 

### Requirements

Excluding the obvious requirements like Java 21 (Minecraft 1.21 and newer) and an IDE or text editor which supports Gradle. You will need to set up all the API keys and credentials needed by the Google Drive API.

### Google API Setup

Firstly, you'll want to follow the [Java Quickstart](https://developers.google.com/drive/api/quickstart/java) guide for the Java Google Drive API then follow the steps below.

First, in the `src/main/resources/assets/world-saver` directory, create a new folder called `keys` and inside it create a new file called `google_api_keys.json`. Then, paste the below text into the file. Then replace "Put your API key here" with your API key.

```
{
  "api_key": "Put your API key here"
}
```

Next, in that same directory, there is where you'll want to put your `credentials.json` file. Once you've done that, World Saver's Google API integration should work properly.