var services = services;
function onBeforePublishClient(obj) {
    // Translate body
    var origText = obj.msgTextInput;
    var translateService = services.get('translateService');
    obj.msgTextOutput = ''
        + translateService.transalate(origText, 'en', 'es')
        + ' (' + origText + ')';

    return obj
}