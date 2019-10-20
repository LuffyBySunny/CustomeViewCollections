package com.sunny.tinkertest.media

import android.media.MediaCodec
import android.media.MediaExtractor
import android.media.MediaFormat
import android.os.Build
import androidx.annotation.RequiresApi
import java.nio.ByteBuffer

/**
 * Created by SunShuo.
 * Date: 2019-09-27
 * Time: 17:03
 */
object AudioCodec {
    public var srcPath : String? = null//源位置
    public var desPath : String? = null//目的位置
    private var mediaDecode : MediaCodec? = null //编码器
    private var mediaEncode : MediaCodec? = null//解码器

    private var codeOver = false
    private var fileTotalSize = 0

    //MediaCodec在此ByteBuffer[]中获取输入数据
    private val decodeInputBuffers : Array<ByteBuffer>?  by lazy {
        mediaDecode?.inputBuffers
    }
    //MediaCodec将解码后的数据放到此ByteBuffer[]中 我们可以直接在这里面得到PCM数据
    private val decodeOutputBuffers : Array<ByteBuffer>? by lazy {
        mediaDecode?.outputBuffers
    }
    private val mediaExtractor :  MediaExtractor by lazy {
        MediaExtractor()
    }

    //用于描述解码得到的byte[]数据的相关信息
    private val decodeBufferInfo : MediaCodec.BufferInfo by lazy {
        MediaCodec.BufferInfo()
    }


    /**
     * 初始化解码器
     */
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun initMediaDecode() {
        mediaExtractor.setDataSource(srcPath?:"")
        //遍历音轨
        for (i in 0 until mediaExtractor.trackCount) {

            val mediaFormat = mediaExtractor.getTrackFormat(i)
            val mime = mediaFormat.getString(MediaFormat.KEY_MIME)

            //如果是音频
            if (mime?.startsWith("audio") == true) {
                //选择音频轨道
                mediaExtractor.selectTrack(i)
                mediaDecode = MediaCodec.createDecoderByType(mime)
                mediaDecode?.configure(mediaFormat, null, null, 0)
                break
            }
        }

        if(mediaDecode == null) {
            return
        }
        //启动，等待数据
        mediaDecode?.start()

        mediaDecode?.setCallback(object : MediaCodec.Callback(){
            override fun onOutputBufferAvailable(codec: MediaCodec, index: Int, info: MediaCodec.BufferInfo) {

            }

            override fun onInputBufferAvailable(codec: MediaCodec, index: Int) {
                val mediaFormat = codec.inputFormat
                val byteBuffer = codec.getInputBuffer(index)
                val sampleSize = mediaExtractor.readSampleData(byteBuffer, 0)
                codec.queueInputBuffer(index, 0, sampleSize, 0, 0)
            }

            override fun onOutputFormatChanged(codec: MediaCodec, format: MediaFormat) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onError(codec: MediaCodec, e: MediaCodec.CodecException) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

        })

    }

    /**
     * 开始解码，将音频解析成原始的PCM数据包
     */

    private fun srcAudioFormatToPCM() {
        //枯燥的循环
        repeat((decodeInputBuffers?.size!! -1)) {
            // 获取可用的inputBuffer -1代表一直等待，0表示不等待 建议-1,避免丢帧
            val inputIndex = mediaDecode?.dequeueInputBuffer(-1)
            if (inputIndex!! < 0) {
                codeOver = true
                return
            }
            //get inputBuffer
            val byteBuffer = decodeInputBuffers?.get(inputIndex)!!

            //clear old data
            byteBuffer.clear()
            //read data to byteBuffer
            val simpleSize = mediaExtractor.readSampleData(byteBuffer, 0)
            //read complete
            if (simpleSize < 0) {
                codeOver = true
            } else {
                //notify mediaDecode start decode
                mediaDecode?.queueInputBuffer(inputIndex, 0, simpleSize, 0, 0)
                mediaExtractor.advance()// goto next point
            }


            //get decoded byteBuffer
            var outputIndex = mediaDecode?.dequeueOutputBuffer(decodeBufferInfo, 100000)
            var outPutBuffer : ByteBuffer? = null
            val chunkPCM = ByteArray(decodeBufferInfo.size)
            while (outputIndex!!>0) {
                // 拿到用于存放PCM数据的Buffer
                outPutBuffer = decodeOutputBuffers?.get(outputIndex)
                //outPutBuffer中的数据读取到chunkPCM中
                outPutBuffer?.get(chunkPCM)
                //清除数据
                outPutBuffer?.clear()

                //然后拿到数据去进行
                //释放缓冲区，因为MediaCodec的缓冲区是有限的
                mediaDecode?.releaseOutputBuffer(outputIndex, false)

                // if outputIndex == -1 then no more data over
                outputIndex = mediaDecode?.dequeueOutputBuffer(decodeBufferInfo, 10000)

            }






        }
     }

}