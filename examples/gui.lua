local M = {
    name = "mymod",
}

M.init = function()
    -- do stuff
    print("Hello lua world!")
end

M.render = function(ctx)
    local window = ctx.createWindow("Test window")

    window.text("Hello world!")

    window.draw()
end

return M
