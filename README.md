# Rename Last Commit Plugin

A lightweight IntelliJ IDEA plugin that allows you to quickly rename your last Git commit directly from the IDE — without using the terminal.


Based on `git commit --amend -m "new message"`


## 📦 Installation

1. Clone this repository
2. Open it in IntelliJ IDEA
3. Run the plugin with:
 ```bash
 ./gradlew clean build runIde
```
4. Open you project in a sandbox where Git has been initiated.
5. Top menu -> Tools -> Rename Last Commit (works only if at least one commit already exists).
6. Enter new commit message.
