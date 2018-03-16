# Ore Stages [![](http://cf.way2muchnoise.eu/290201.svg)](https://minecraft.curseforge.com/projects/290201) [![](http://cf.way2muchnoise.eu/versions/290201.svg)](https://minecraft.curseforge.com/projects/290201)

This mod is an addon for the GameStage API. It allows for blocks in the world, like ores, to be put into game stages. You should check out the GameStage API mod's description for more info. To give a brief run down, stages are parts of the progression system set up by the modpack or server. Stages are given to players through a command, which is typically ran by a questing mod, advancement, or even a Command Block.

[![Nodecraft](https://i.imgur.com/sz9PUmK.png)](https://nodecraft.com/r/darkhax)    
This project is sponsored by Nodecraft. Use code [Darkhax](https://nodecraft.com/r/darkhax) for 30% off your first month of service!

## Setup
This mod uses CraftTweaker for configuration.

## What happens when staged?
If a player does not have the right stage for the block
- The block will look like the block it is hidden as. 
- The player will not be able to right click the block.
- The block will drop the items it's replacement would drop.
- The block will take as long to mine as it's replacement.

In situations where no player is present, it will be assumed that there is no stage. This is referred to as defaulting behavior. 

## CraftTweaker methods

This method can be used to replace a block with vanilla stone.
`mods.OreTiers.addReplacement(String stage, IIngredient original);`

This method can be used to replace a block with another block.
`mods.OreTiers.addReplacement(String stage, IIngredient original, IItemStack replacement);`

This method can be used to replace a block with another block by specifying exact block ids. The above two methods only work for blocks with items.
`mods.OreTiers.addReplacementById(String stage, String original, String replacement);`

The following methods are used to add a replacement which do not use the defaulting behavior. Meaning if no player broke the block (water, explosion, machine) the block will break as if it was not hidden. 

`mods.OreTiers.addNonDefaultingReplacement(String stage, IIngredient original);`

`mods.OreTiers.addNonDefaultingReplacement(String stage, IIngredient original, IItemStack replacement);`

`mods.OreTiers.addNonDefaultingReplacementById(String stage, String original, String replacement);`

## Example Script
```
mods.OreTiers.addReplacementById("one", "minecraft:potatoes:*", "minecraft:tallgrass:2");
mods.OreTiers.addReplacementById("two", "minecraft:wheat:*", "minecraft:carrots:3");
mods.OreTiers.addReplacementById("three", "minecraft:nether_wart:*", "minecraft:beetroots");
mods.OreTiers.addReplacement("four", <minecraft:dirt>, <minecraft:stone>);
mods.OreTiers.addReplacement("one", <minecraft:obsidian>, <minecraft:cobblestone>);
mods.OreTiers.addNonDefaultingReplacement("one", <minecraft:torch:*>, <minecraft:redstone_torch>);
mods.OreTiers.addReplacement("five", <minecraft:furnace:*>, <minecraft:stone>);
```