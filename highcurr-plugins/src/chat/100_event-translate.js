var services = services;
function onBeforePublishClient(obj) {
    // Translate body
    var origText = obj.body;
    var translateService = services.get('translateService');
    obj.body = translateService.transalate(origText, 'en', 'es') + ' (' + origText + ')';

    return obj
}