package highcurr.highcurrws.vertx.verticle.wsserver

import com.google.cloud.translate.Translate
import com.google.cloud.translate.TranslateOptions
import com.google.cloud.translate.Translation
import org.springframework.stereotype.Service

@Service
class TranslateService {

    val translate: Translate = TranslateOptions.getDefaultInstance().service

    fun transalate(input: String, sourceLang: String = "en", targetLang: String = "en"): String {
        var translation: Translation = translate.translate(
                input,
                Translate.TranslateOption.sourceLanguage(sourceLang),
                Translate.TranslateOption.targetLanguage(targetLang)
        )
        return translation.translatedText
    }
}