package xingkong.loopicserver.module.utils

import java.net.Inet4Address

object NetUtil {

    fun getIPAddress() = Inet4Address.getLocalHost().hostAddress
}