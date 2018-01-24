//load("https://raw.github.com/lodash/lodash/3.10.1/lodash.js");
var _ = require("lodash/lodash");

function onBeforePublishClient(obj) {
    // Capitalize body
    obj.msgTextOutput = obj.msgTextOutput.toUpperCase();

    return obj
}