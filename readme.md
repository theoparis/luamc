# LuaCraft

A [Lua](https://www.lua.org/) based [mod](https://en.wikipedia.org/wiki/Mod_(computing))
for [Minecraft](https://minecraft.net/). This mod allows you to create lua scripts that can be run in the game.

## Requirements

- [Fabric](https://fabricmc.net/)
- [FabricAPI](https://fabricmc.net/wiki/fabricapi/)

## Creating A Mod

```lua
local M = {
    name = "mymod",
}

M.init = function()
    -- do stuff
    print("Hello lua world!")
end

return M
```

You can then place this file in the `mods/` folder, and it will be loaded automatically.

## Examples

You can find some examples in the [examples folder](examples).

