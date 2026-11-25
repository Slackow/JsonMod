package com.slackow.jsonmod;

import com.llamalad7.mixinextras.lib.antlr.runtime.CharStreams;
import com.llamalad7.mixinextras.lib.antlr.runtime.CommonTokenStream;
import net.fabricmc.api.ModInitializer;

import net.minecraft.resources.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsonMod implements ModInitializer {
	public static final String MOD_ID = "jsonmod";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {

		var stream = new CommonTokenStream(new JsonLexer(CharStreams.fromString("[90, 80, \"uhoh\"]")));
		LOGGER.info("{}", new JsonNbtVisitor().visitJson_file(new JsonParser(stream).json_file()));
		LOGGER.info("Hello Fabric world!");
	}

	public static Identifier id(String path) {
		return Identifier.fromNamespaceAndPath(MOD_ID, path);
	}
}
