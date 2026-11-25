# JsonMod

Proof of concept mod that hijacks MixinExtras's Bundled Antlr Runtime to do Antlr things

ProGuard complicates this by removing some classes and fields the generated parser would normally use, so some workarounds are in the build script and antlr file itself, but otherwise it works exactly as expected, and when built, it prints out the data.

It just parses JSON into snbt, which is pretty redundant since there's at least 3 JSON parsers in fabric/minecraft already, and I'm pretty sure most valid json would parse into the same nbt structure anyway. Lol.

The neat thing is that this mod can be loaded on Fabric despite not bundling its own Antlr Runtime, and you can do this with Neoforge too.

In practice, you should not depend on caught dependencies like this, especially when they've been repackaged. It's just kinda funny.