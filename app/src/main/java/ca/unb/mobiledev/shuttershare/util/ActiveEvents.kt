package ca.unb.mobiledev.shuttershare.util

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.icu.util.Calendar
import android.util.Log
import androidx.camera.core.impl.utils.ContextUtil.getBaseContext
import org.json.JSONArray
import org.json.JSONObject
import java.io.*
import java.nio.charset.StandardCharsets


class ActiveEvents {
    private val finishedEvents = FinishedEvents()

    fun addEvent(context: Context, eventCode: String, eventName: String, endTimeInMillis: Long) {
        // TODO add the event
        val jsonString = """{
            "$EVENT_CODE": "$eventCode",
            "$EVENT_NAME": "$eventName",
            "$END_TIME": $endTimeInMillis
        }""".trimMargin()

        try {
//            var fileText: String? = ""
            if(fileExists(context)) {
                val fileText = loadJSONFromInternalStorage(context)

                // Create a JSON Object from file contents and add new JSON Event object to it
                val jsonObject = JSONObject(fileText!!)
                val eventJSONObject = JSONObject(jsonString)
                jsonObject.accumulate("active_events", eventJSONObject)

                writeJSONToInternalStorage(context, jsonObject.toString())
                Log.d(TAG, "In file: " + jsonObject.toString())
            }
            else {
                val emptyJsonObject = JSONObject()
                val newJSONArray = JSONArray()
                val eventJSONObject = JSONObject(jsonString) // the event json
                newJSONArray.put(eventJSONObject)
                emptyJsonObject.accumulate("active_events", newJSONArray)
                Log.d(TAG, "Empty JSONObject: " + emptyJsonObject.toString())

                writeJSONToInternalStorage(context, emptyJsonObject.toString())

                //for testing
//                fileText = loadJSONFromInternalStorage(context)
            }
//            Log.d("FileContent", fileText!!)
        }
        catch (e: Exception) {
            e.printStackTrace()
            Log.e(TAG, "somethin happened")
        }
    }

    fun removeExpiredEvents(context: Context) {
        if(fileExists(context)){
            val jsonText = loadJSONFromInternalStorage(context)
            val jsonObject = JSONObject(jsonText)

            val jsonArray = jsonObject.getJSONArray(ACTIVE_EVENTS)

            var toBeKeptEvents = JSONArray()
            val nowTimestamp = Calendar.getInstance().timeInMillis
            var endTime : Long = 0

            val emptyJsonObject = JSONObject()

            for (i in 0 until jsonArray.length()) {
                // Create a JSON Object from individual JSON Array element
                val elementObject = jsonArray.getJSONObject(i)

                //get endTime
                endTime = elementObject.get(END_TIME) as Long

                Log.d(TAG, "End Time: " + endTime + ", NOW: " + nowTimestamp)
                // if endTime is not expired, keep this element
                if (endTime > nowTimestamp) {
                    toBeKeptEvents.put(elementObject)
                }
                else {
                    // add to FinishedEvents
                    Log.d(TAG, "Add to FinishedEvents: " + elementObject.toString())
                    finishedEvents.addEvent(context, elementObject.toString())
                }
            }

            emptyJsonObject.accumulate("active_events", toBeKeptEvents)
            Log.d(TAG, "Keep JSONObjects: " + emptyJsonObject.toString())


            writeJSONToInternalStorage(context, emptyJsonObject.toString())
        }
    }

    fun getArrayOfEventNames(context: Context): Array<String?> {
        if(fileExists(context)){
            val jsonText = loadJSONFromInternalStorage(context)
            val jsonObject = JSONObject(jsonText)

            var currentEventName = ""
            val jsonArray = jsonObject.getJSONArray(ACTIVE_EVENTS)
            var eventNameArray = arrayOfNulls<String>(jsonArray.length())
            for (i in 0 until jsonArray.length()) {
                // Create a JSON Object from individual JSON Array element
                val elementObject = jsonArray.getJSONObject(i)

                currentEventName = elementObject.getString(EVENT_NAME)

                // Put event name in array
                eventNameArray[i] = currentEventName
            }

            Log.d(TAG, jsonArray.toString())
            return eventNameArray
        }

        return arrayOfNulls<String>(0)
    }

    // Assumes that the eventNames are unique...
    fun getEventCode(context: Context, eventName: String): String {
        val jsonText = loadJSONFromInternalStorage(context)
        val jsonObject = JSONObject(jsonText)

        var eventCode = ""
        var currentEventName = ""
        val jsonArray = jsonObject.getJSONArray(ACTIVE_EVENTS)
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
            context.openFileOutput(filename, MODE_PRIVATE).use {
                it.write(fileContents.toByteArray())
            }
        }
        catch (ex: IOException) {
            ex.printStackTrace()
        }
    }

    fun fileExists(context: Context): Boolean {
        val file = context.getFileStreamPath(JSON_FILE)
        Log.d(TAG, "active_events.json exists: " + file.exists())
        return file.exists()
    }

    companion object {
        private const val TAG = "ActiveEvents"
        private const val JSON_FILE = "active_events.json"
        private const val ACTIVE_EVENTS = "active_events"
        private const val EVENT_CODE = "event_code"
        private const val EVENT_NAME = "event_name"
        private const val END_TIME = "event_endTimestamp"
    }
}