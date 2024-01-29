import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.ui.Messages
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException
import org.jetbrains.annotations.NotNull
import com.google.gson.Gson

data class CodePayload(val code: String)
class SendCodeToServerAction : AnAction() {
    override fun actionPerformed(e: @NotNull AnActionEvent) {
        val editor: Editor? = e.getData(CommonDataKeys.EDITOR)
        val selectedText = editor?.selectionModel?.selectedText

        selectedText?.let {
            sendCodeToServer(it)
        } ?: run {
            Messages.showErrorDialog(e.project, "No text selected", "Error")
        }
    }

    private fun sendCodeToServer(code: String) {
        val client = OkHttpClient()
        val gson = Gson()
        val payload = CodePayload(code)
        val json = gson.toJson(payload)
        val mediaType = "application/json; charset=utf-8".toMediaType()
        val body = json.toRequestBody(mediaType)
        val request = Request.Builder()
                .url("http://localhost:3000/api/v1/blueprints")
                .post(body)
                .build()

        client.newCall(request).execute().use { response ->
            if(!response.isSuccessful) {
                throw IOException("Unexpected code $response")
            }

            Messages.showInfoMessage("Code sent to server", "Success")
        }
    }

}