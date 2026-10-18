# Exposure Export Any

Client-side NeoForge 1.21.1 addon for the Exposure camera mod.
Press a key (default **F8**) to save the photograph you are **holding** or **viewing full-screen** as a PNG,
even if someone else took it. No commands or operator permissions needed.

- Hold a photograph and press the key, or right-click it to open the viewer and press the key.
- Holding a stack of photographs exports every photo in the stack.
- Files go to `<game folder>/exposures/` (inside a world-name subfolder, if Exposure's config has that on),
  with the same size multiplier and paper look (regular or aged) that Exposure uses for its own exports.
- Change the key in Options > Controls > "Exposure Export Any".

## Install

Drop the built jar into the `mods` folder next to Exposure. It only needs to be installed on your own computer,
not on the server.

## Build the jar

Needs JDK 21 and Gradle 8.8+ (or open the folder in IntelliJ IDEA, which sets this up for you).

    gradle build

The jar ends up in `build/libs/`.

No Gradle on your computer? Push this folder to a GitHub repository. The included workflow
(`.github/workflows/build.yml`) builds it, and you download the jar from the run's "Artifacts".

## Notes

- Compiled against Exposure 1.9.16. It uses Exposure's own export classes, so a much newer or older
  Exposure may rename something. If the build or the game complains about a missing class, say which.
- The photograph must have finished loading. If you see "still loading", look at it for a second and press again.
