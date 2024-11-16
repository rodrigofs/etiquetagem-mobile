package com.esoft.emobile.support

import com.esoft.emobile.domain.model.Tag
import com.sewoo.jpos.command.ZPLConst
import com.sewoo.jpos.printer.ZPLPrinter
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class LabelPrinterService(
    private val printer: ZPLPrinter,
    private val acronym: String,
    private val plate: String
) {

    fun printLabels(tags: List<Tag>) {
        printer.setCharSet("UTF-8")
        printer.setInternationalFont(0)
        val dt = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))

        tags.forEach { tag ->
            preparePrinter()
            printLabtagDetails(tag, dt)
            printer.endPage(1)
        }
    }

    private fun preparePrinter() {
        val paperType = ZPLConst.SENSE_GAP
        printer.setupPrinter(ZPLConst.ROTATION_180, paperType, 792, 566)
    }

    private fun printLabtagDetails(tag: Tag, dateTime: String) {
        printer.startPage()

        // Retângulo da Etiqueta
        printer.printRectangle(20, 20, 750, 505, 1, 'B', 0)

        // Linhas Horizontais
        printer.printRectangle(20, 140, 750, 1, 1, 'B', 0)
        printer.printRectangle(20, 300, 750, 1, 1, 'B', 0)
        printer.printRectangle(20, 365, 750, 1, 1, 'B', 0)

        // Linhas Verticais
        printer.printRectangle(480, 20, 1, 120, 1, 'B', 0)
        printer.printRectangle(420, 140, 1, 160, 1, 'B', 0)
        printer.printRectangle(606, 140, 1, 120, 1, 'B', 0)

        // Informações da Impressão e Nro da Nota
        printer.printText(
            ZPLConst.FONT_A,
            ZPLConst.ROTATION_0,
            15,
            12,
            30,
            30,
            ("IMPRESSO EM " + tag.orgSigla).toString() + " " + dateTime
        )
        printer.printText(ZPLConst.FONT_A, ZPLConst.ROTATION_0, 15, 12, 490, 30, "VOLUMES")
        printer.printText(ZPLConst.FONT_H, ZPLConst.ROTATION_0, 50, 25, 30, 70, "NF:" + tag.nf)
        printer.printText(ZPLConst.FONT_G, ZPLConst.ROTATION_0, 15, 8, 530, 70, tag.item)

        // Origem
        printer.printText(ZPLConst.FONT_A, ZPLConst.ROTATION_0, 15, 12, 30, 160, "ORIGEM")
        printer.printText(
            ZPLConst.FONT_G,
            ZPLConst.ROTATION_0,
            15,
            12,
            140,
            190,
            if (tag.orgSigla.isEmpty()) acronym else tag.orgSigla
        )
        printer.printText(
            ZPLConst.FONT_D,
            ZPLConst.ROTATION_0,
            15,
            12,
            30,
            280,
            limitString(tag.emitNome, 32)
        )

        // Destino
        printer.printText(ZPLConst.FONT_A, ZPLConst.ROTATION_0, 15, 12, 430, 160, "DESTINO")
        printer.printText(ZPLConst.FONT_G, ZPLConst.ROTATION_0, 15, 12, 460, 190, tag.dstSigla)
        printer.printText(
            ZPLConst.FONT_D,
            ZPLConst.ROTATION_0,
            15,
            12,
            430,
            280,
            limitString(tag.destNome, 27)
        )

        // Rota
        printer.printText(ZPLConst.FONT_A, ZPLConst.ROTATION_0, 15, 12, 615, 160, "ROTA")
        printer.printText(ZPLConst.FONT_E, ZPLConst.ROTATION_0, 60, 12, 650, 190, tag.dstRota)

        // Destino
        printer.printText(ZPLConst.FONT_A, ZPLConst.ROTATION_0, 15, 12, 30, 315, "DESTINO")
        printer.printText(
            ZPLConst.FONT_D,
            ZPLConst.ROTATION_0,
            15,
            12,
            30,
            340,
            (tag.destLogradouro + ": nro " + tag.destNumero).toString() + "-" + tag.destCidade
        )

        // Código de Barras - Ajustar tamanho e posição
        val byParam = ArrayList<String>()
        byParam.add("2.5")  // Largura da barra aumentada
        byParam.add("90")   // Altura aumentada
        byParam.add("100")  // Tamanho da fonte aumentada
        printer.printBarcode(ZPLConst.BARCODE_Code128, byParam, 90, 400, tag.etiqueta)

        // APP
        printer.printText(ZPLConst.FONT_A, ZPLConst.ROTATION_0, 15, 12, 620, 400, "APP")
        printer.printText(ZPLConst.FONT_B, ZPLConst.ROTATION_0, 15, 12, 620, 430, "Emissor")
        printer.printText(ZPLConst.FONT_A, ZPLConst.ROTATION_0, 15, 12, 620, 470, "Veiculo")
        printer.printText(ZPLConst.FONT_B, ZPLConst.ROTATION_0, 15, 12, 620, 500, plate)

        // Localize sua carga em: www.transminuano.com.br
        printer.printText(
            ZPLConst.FONT_A,
            ZPLConst.ROTATION_0,
            15,
            12,
            30,
            540,
            "localize sua carga em: www.transminuano.com.br"
        )

        printer.endPage(1)
    }

    private fun printLabtagDetailsBKP(tag: Tag, dateTime: String) {
        printer.startPage()
        //Retangulo da Etiqueta
        printer.printRectangle(20, 20, 750, 505, 1, 'B', 0)
        //Linhas Horizontais
        printer.printRectangle(20, 140, 750, 1, 1, 'B', 0)
        printer.printRectangle(20, 300, 750, 1, 1, 'B', 0)
        printer.printRectangle(20, 365, 750, 1, 1, 'B', 0)
        //Linhas Verticais
        printer.printRectangle(480, 20, 1, 120, 1, 'B', 0)
        printer.printRectangle(420, 140, 1, 160, 1, 'B', 0)
        printer.printRectangle(606, 140, 1, 120, 1, 'B', 0)

        //Informações da Impressão e Nro da Nota
        printer.printText(
            ZPLConst.FONT_A,
            ZPLConst.ROTATION_0,
            15,
            12,
            30,
            30,
            ("IMPRESSO EM " + tag.orgSigla).toString() + " " + dateTime
        )
        printer.printText(
            ZPLConst.FONT_A,
            ZPLConst.ROTATION_0,
            15,
            12,
            490,
            30,
            "VOLUMES"
        )
        printer.printText(
            ZPLConst.FONT_H,
            ZPLConst.ROTATION_0,
            50,
            25,
            30,
            70,
            "NF:" + tag.nf
        )
        printer.printText(ZPLConst.FONT_G, ZPLConst.ROTATION_0, 15, 8, 530, 70, tag.item)

        //Origem
        printer.printText(
            ZPLConst.FONT_A,
            ZPLConst.ROTATION_0,
            15,
            12,
            30,
            160,
            "ORIGEM"
        )
        printer.printText(
            ZPLConst.FONT_G,
            ZPLConst.ROTATION_0,
            15,
            12,
            140,
            190,
            if (tag.orgSigla.isEmpty()) acronym else tag.orgSigla
        )
        printer.printText(
            ZPLConst.FONT_D,
            ZPLConst.ROTATION_0,
            15,
            12,
            30,
            280,
            limitString(tag.emitNome, 32)
        )

        //Destino
        printer.printText(
            ZPLConst.FONT_A,
            ZPLConst.ROTATION_0,
            15,
            12,
            430,
            160,
            "DESTINO"
        )
        printer.printText(
            ZPLConst.FONT_G,
            ZPLConst.ROTATION_0,
            15,
            12,
            460,
            190,
            tag.dstSigla
        )
        printer.printText(
            ZPLConst.FONT_D,
            ZPLConst.ROTATION_0,
            15,
            12,
            430,
            280,
            limitString(tag.destNome, 27)
        )

        //Rota
        printer.printText(ZPLConst.FONT_A, ZPLConst.ROTATION_0, 15, 12, 615, 160, "ROTA")
        printer.printText(
            ZPLConst.FONT_E,
            ZPLConst.ROTATION_0,
            60,
            12,
            650,
            190,
            tag.dstRota
        )

        //Destino
        printer.printText(
            ZPLConst.FONT_A,
            ZPLConst.ROTATION_0,
            15,
            12,
            30,
            315,
            "DESTINO"
        )
        printer.printText(
            ZPLConst.FONT_D,
            ZPLConst.ROTATION_0,
            15,
            12,
            30,
            340,
            (tag.destLogradouro + ": nro " + tag.destNumero).toString() + "-" + tag.destCidade
        )

        //Codigo de Barras
        val byParam = ArrayList<String>()
        byParam.add("2.0")
        byParam.add("70")
        byParam.add("80")
        printer.printBarcode(ZPLConst.BARCODE_Code128, byParam, 90, 400, tag.etiqueta)
        //printer.directCommand("^B3^FO110,400^BC^FD" + tag.etiqueta + "^FS");
        //APP
        printer.printText(ZPLConst.FONT_A, ZPLConst.ROTATION_0, 15, 12, 620, 400, "APP")
        printer.printText(
            ZPLConst.FONT_B,
            ZPLConst.ROTATION_0,
            15,
            12,
            620,
            430,
            "Emissor"
        )
        printer.printText(
            ZPLConst.FONT_A,
            ZPLConst.ROTATION_0,
            15,
            12,
            620,
            470,
            "Veiculo"
        )
        printer.printText(
            ZPLConst.FONT_B,
            ZPLConst.ROTATION_0,
            15,
            12,
            620,
            500,
            plate
        )

        //Localize sua carga em: www.transminuano.com.br
        printer.printText(
            ZPLConst.FONT_A,
            ZPLConst.ROTATION_0,
            15,
            12,
            30,
            540,
            "localize sua carga em: www.transminuano.com.br"
        )

        printer.endPage(1)
    }

    private fun limitString(str: String, limit: Int): String {
        return if (str.length > limit) str.substring(0, limit) else str
    }
}