package com.example.myapplication

import android.util.Log
import android.util.Xml
import com.example.myapplication.Model.Entry
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import java.io.IOException
import java.io.InputStream

class XmlParser {
    private val ns: String? = null

    @Throws(XmlPullParserException::class, IOException::class)
    fun parse(inputStream: InputStream): List<*> {
        inputStream.use { inputStream ->
            val parser: XmlPullParser = Xml.newPullParser()
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false)
            parser.setInput(inputStream, null)
            parser.nextTag()
            return readFeed(parser)
        }

    }


    @Throws(XmlPullParserException::class, IOException::class)
    private fun readFeed(parser: XmlPullParser): List<Entry> {
        val entries = mutableListOf<Entry>()

        parser.require(XmlPullParser.START_TAG, ns, "MPD")
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.eventType != XmlPullParser.START_TAG) {
                continue
            }
                if(parser.name =="Period")
                {
                    while(parser.next() != XmlPullParser.END_TAG)
                    {
                        if (parser.eventType != XmlPullParser.START_TAG) {
                            continue
                        }
                        if(parser.name == "AdaptationSet")
                        {
                            while(parser.next() != XmlPullParser.END_TAG)
                            {
                                if (parser.eventType != XmlPullParser.START_TAG) {
                                    continue
                                }
                                if(parser.name == "Representation")
                                {
                                    entries.add(readEntry(parser))
                                }
                                else
                                {
                                    skip(parser)
                                }

                            }
                        }
                        else
                        {
                            skip(parser)
                        }
                    }
                } else {
                skip(parser)
            }
        }
        Log.d("Extracted", entries.toString())

        return entries

    }


    @Throws(XmlPullParserException::class, IOException::class)
    private fun readEntry(parser: XmlPullParser): Entry {
        parser.require(XmlPullParser.START_TAG, ns, "Representation")
        var BaseURL: String? = null
        var FBQualityLabel:String? = null
        var AudioLink : String?=null


        val mimetype=parser.getAttributeValue(null,"mimeType")


        if(parser.name == "Representation")
        {
            if(mimetype=="video/mp4")
            {
                FBQualityLabel=parser.getAttributeValue(null,"FBQualityLabel")

                while (parser.next() != XmlPullParser.END_TAG) {

                    if (parser.eventType != XmlPullParser.START_TAG) {
                        continue
                    }
                    when (parser.name) {
                        "BaseURL" -> BaseURL = readLink(parser)
                        else -> skip(parser)
                    }
                }

            }
            else
            {
                FBQualityLabel="Audio"

                while (parser.next() != XmlPullParser.END_TAG) {

                    if (parser.eventType != XmlPullParser.START_TAG) {
                        continue
                    }
                    when (parser.name) {
                        "BaseURL" -> BaseURL = readLink(parser)
                        else -> skip(parser)
                    }
                }

            }

        }

        return Entry(BaseURL,FBQualityLabel)
    }
    @Throws(IOException::class, XmlPullParserException::class)
    private fun readLink(parser: XmlPullParser): String {
        parser.require(XmlPullParser.START_TAG, ns, "BaseURL")
        val link = readText(parser)
        parser.require(XmlPullParser.END_TAG, ns, "BaseURL")
        return link
    }
    @Throws(IOException::class, XmlPullParserException::class)
    private fun readText(parser: XmlPullParser): String {
        var result = ""
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.text
            parser.nextTag()
        }
        return result
    }

    @Throws(XmlPullParserException::class, IOException::class)
    private fun skip(parser: XmlPullParser) {
        if (parser.eventType != XmlPullParser.START_TAG) {
            throw IllegalStateException()
        }
        var depth = 1
        while (depth != 0) {
            when (parser.next()) {
                XmlPullParser.END_TAG -> depth--
                XmlPullParser.START_TAG -> depth++
            }
        }
    }
}
