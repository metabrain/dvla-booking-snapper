package com.metabrain.dvla.bookingsnapper

import mu.KotlinLogging
import org.apache.http.client.HttpClient
import org.apache.http.client.config.CookieSpecs
import org.apache.http.client.config.RequestConfig
import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.BasicCookieStore
import org.apache.http.impl.client.HttpClients
import org.apache.http.message.BasicHeader

private val LOG = KotlinLogging.logger {}

data class Monitor(
        val email: String,
        val licenseNum: String,
        val postCode: String,
        val centers: List<String> // TODO are center name strings unique..?
)


fun main(args: Array<String>) {
    LOG.info("Starting to check..")

    val monitors = listOf(
            Monitor(
                    email = "fakeemailhere@gmail.com",
                    licenseNum = "11111111111111",
                    postCode = "E15",
                    centers = listOf("Goodmayes")
            )
    )

//    while(true) {
        monitors.forEach {
            it.refresh()
        }
//        Thread.sleep(10_000)
//    }


}

fun Monitor.refresh() {
    val cookieJar = BasicCookieStore()
    val client = HttpClients.custom()
            .setDefaultRequestConfig(RequestConfig.custom()
                    .setCookieSpec(CookieSpecs.STANDARD).build())
            .setDefaultCookieStore(cookieJar)
            .setUserAgent("Mozilla/5.0 (Windows NT 6.3; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/68.0.3440.106 Safari/537.36'")
            .setDefaultHeaders(mutableListOf(
                    BasicHeader("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8"),
                    BasicHeader("accept-encoding", "gzip, deflate, br"),
                    BasicHeader("upgrade-insecure-requests", "1"),
                    BasicHeader("accept-language", "en-GB,en;q=0.9,en-US;q=0.8,pt;q=0.7")
            ))
            .build()

    (1..1).forEach {
        client.get("https://www.gov.uk/book-driving-test") // needed to create the cookies so we are not a "robot"
        LOG.info("COOKIES:"+cookieJar.cookies.toString())
        client.get("https://driverpracticaltest.direct.gov.uk/application?_ga=2.241556418.1773252949.1535234280-895705372.1535234280") // needed to create the cookies so we are not a "robot"


    }


}

private fun HttpClient.get(url: String): String {
    val page = execute(
            HttpGet(url)
    )
    LOG.info { "\n\n\n" }
    LOG.info(page.toString())
    val content = page.entity.content.reader(kotlin.text.Charsets.UTF_8).readText()
    LOG.info(content)
    LOG.info(page.allHeaders.joinToString(""))

    return content
}

//
//class BankingCsvAggregator : CliktCommand() {
//    val barclaysFile: String? by option(
//            names = *arrayOf("--barclays"),
//            help = "For Barclays CSV path exported from their website"
//
//    )
//
//    val monzoFile: String? by option(
//            names = *arrayOf("--monzo"),
//            help = "For Monzo CSV path exported from their app"
//    )
//
//    val monzoPrepaidFile: String? by option(
//            names = *arrayOf("--monzo-prepaid"),
//            help = "For Monzo prepaid (less info) CSV path exported from their app"
//    )
//
//    override fun run() {
//
//        val monzo: Monzo? = if(monzoFile!=null || monzoPrepaidFile!=null) {
//            Monzo.parseTxnFromCsvs(
//                    if (monzoFile != null) CsvParser.parseLines(Files.readAllLines(Paths.get(monzoFile), Charsets.UTF_8)) else CsvFile.EMPTY,
//                    if (monzoPrepaidFile != null) CsvParser.parseLines(Files.readAllLines(Paths.get(monzoPrepaidFile), Charsets.UTF_8)) else CsvFile.EMPTY
//            )
//        } else null
//
//        val barclays: Barclays? = if(barclaysFile!=null) {
//            Barclays.parseTxnFromCsvs(
//                    CsvParser.parseLines(Files.readAllLines(Paths.get(barclaysFile), Charsets.ISO_8859_1))
//            )
//        } else null
//
//        val txns: List<Txn> =
//                emptyList<Txn>() +
//                        (barclays?.txns() ?: emptyList()) +
//                        (monzo?.txns() ?: emptyList())
//        //                    .sortedBy { it.amount.toDouble().absoluteValue }
//        //                    .filter { txn -> txn.amount.toDouble()>0.0 }
//
//        txns.forEach { println(it as Txn) }
//
//        println()
//        println()
//        println()
//        println("  TOTAL EARNED: ${txns.earned()}")
//        println("- TOTAL SPENT: ${txns.spent()}")
//        println("  ----------------")
//        println("  BALANCE: ${txns.balance()}")
//
//    }


