package ca.unb.mobiledev.shuttershare.util

import android.content.Context
import android.util.Log
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.IOException

class FinishedEvents {

    fun addEvent(context: Context, jsonString: String) {
        try {
            if(fileExists(context)) {
                val fileText = loadJSONFromInternalStorage(context)

                // Create a JSON Object from file contents and add new JSON Event object to it
                val jsonObject = JSONObject(fileText!!)
                val eventJSONObject = JSONObject(jsonString)
                jsonObject.accumulate(FINISHED_EVENTS, eventJSONObject)

                writeJSONToInternalStorage(context, jsonObject.toString())
                Log.d(TAG, "In file: " + jsonObject.toString())
            }
            else {
                val emptyJsonObject = JSONObject()
                val newJSONArray = JSONArray()
                val eventJSONObject = JSONObject(jsonString) // the event json
                newJSONArray.put(eventJSONObject)
                emptyJsonObject.accumulate(FINISHED_EVENTS, newJSONArray)
                Log.d(TAG, "Empty JSONObject: " + emptyJsonObject.toString())

                writeJSONToInternalStorage(context, emptyJsonObject.toString())
            }
        }
        catch (e: Exception) {
            e.printStackTrace()
            Log.e(TAG, "somethin happened")
        }
    }

    fun getArrayOfEventNames(context: Context): Array<String?> {
        val jsonText = loadJSONFromInternalStorage(context)
        val jsonObject = JSONObject(jsonText)

        var currentEventName = ""
        val jsonArray = jsonObject.getJSONArray(FINISHED_EVENTS)
        var eventNameArray = arrayOfNulls<String>(jsonArray.length())
        for (i in 0 until jsonArray.length()) {
            // Create a JSON Object from individual JSON Array element
            val elementObject = jsonArray.getJSONObject(i)

            currentEventName = elementObject.getString(EVENT_NAME)

            // Put event name in array
            eventNameArray[i] = currentEventName
        }

        return eventNameArray
    }

    fun getArrayOfEventCodes(context: Context): Array<String?> {
        val jsonText = loadJSONFromInternalStorage(context)
        val jsonObject = JSONObject(jsonText)

        var currentEventCode = ""
        val jsonArray = jsonObject.getJSONArray(FINISHED_EVENTS)
        var eventCodeArray = arrayOfNulls<String>(jsonArray.length())
        for (i in 0 until jsonArray.length()) {
            // Create a JSON Object from individual JSON Array element
            val elementObject = jsonArray.getJSONObject(i)

            currentEventCode = elementObject.getString(EVENT_CODE)

            // Put event code in array
            eventCodeArray[i] = currentEventCode
        }

        return eventCodeArray
    }

    fun getEventCode(context: Context, eventName: String): String {
        val jsonText = loadJSONFromInternalStorage(context)
        val jsonObject = JSONObject(jsonText)

        var eventCode = ""
        var currentEventName = ""
        val jsonArray = jsonObject.getJSONArray(FINISHED_EVENTS)
        var eventNameArray = arrayOfNulls<String>(jsonArray.length())
        for (i in 0 until jsonArray.length()) {
            // Create a JSON Object from individual JSON Array element
            val elementObject = jsonArray.getJSONObject(i)

            currentEventName = elementObject.getString(EVENT_NAME)

            if(currentEventName.equals(eventName)) {
                eventCode = elementObject.getString(EVENT_CODE)
            }
        }

        Log.d(TAG, jsonArray.toString())

        return eventCode
    }

    private fun loadJSONFromInternalStorage(context: Context): String? {
        try {
            val file = JSON_FILE.let { File(context.filesDir, it) }

            var line: String?
            val stringBuilder = StringBuilder()

            val fileReader = FileReader(file)
            val bufferedReader = BufferedReader(fileReader)
            while (bufferedReader.readLine().also { line = it } != null) {
                stringBuilder.append(line)
            }

            return stringBuilder.toString()
        }
        catch (ex: IOException) {
            ex.printStackTrace()
            return null
        }
    }

    private fun writeJSONToInternalStorage(context: Context, jsonString: String) {
        try {
            val filename = JSON_FILE
            val fileContents = jsonString
            context.openFileOutput(filename, Context.MODE_PRIVATE).use {
                it.write(fileContents.toByteArray())
            }
        }
        catch (ex: IOException) {
            ex.printStackTrace()
        }
    }

    fun fileExists(context: Context): Boolean {
        val file = context.getFileStreamPath(JSON_FILE)
        return file.exists()
    }

    companion object {
        private const val TAG = "FinishedEvents"
        private const val JSON_FILE = "finished_events.json"
        private const val FINISHED_EVENTS = "finished_events"
        private const val EVENT_CODE = "event_code"
        private const val EVENT_NAME = "event_name"
        private const val END_TIME = "event_endTimestamp"
    }
}