import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.ui.Messages
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException
import com.google.gson.Gson
import org.jetbrains.annotations.NotNull

data class ServerResponse(val result: String)

class SubmitDescriptionAction : AnAction() {
    override fun actionPerformed(e: @NotNull AnActionEvent) {
        val project = e.project ?: return
        val editor = e.getData(CommonDataKeys.EDITOR) ?: return
        val document = editor.document
        val selectedText = editor.selectionModel.selectedText ?: return

        ApplicationManager.getApplication().executeOnPooledThread {
            try {
                val replacedText = sendDescriptionAndGetCode(selectedText)
                WriteCommandAction.runWriteCommandAction(project) {
                    val startOffset = editor.selectionModel.selectionStart
                    val endOffset = editor.selectionModel.selectionEnd
                    document.replaceString(startOffset, endOffset, replacedText)
                }
            } catch (ex: IOException) {
                ApplicationManager.getApplication().invokeLater {
                    Messages.showErrorDialog(project, ex.message, "Error")
                }
            }
        }
    }

    private fun sendDescriptionAndGetCode(description: String): String {
        val client = OkHttpClient()
        val mediaType = "application/json; charset=utf-8".toMediaType()
        val body = "{\"description\": \"$description\"}".toRequestBody(mediaType)
        val request = Request.Builder()
                .url("http://localhost:3000/api/v1/blueprint_variants")
                .post(body)
                .build()

        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) {
                throw IOException("Unexpected code $response")
            }

            val responseBody = response.body?.string() ?: ""
            return parseResponse(responseBody)
        }
    }

    private fun parseResponse(response: String): String {
        val gson = Gson()
        val serverResponse = gson.fromJson(response, ServerResponse::class.java)

        return serverResponse.result
    }
}