var services = services;
function onBeforePublishClient(obj) {
    // Translate body
    obj.body = services.get('translateService').transalate(obj.body, 'en', 'es');

    return obj
}