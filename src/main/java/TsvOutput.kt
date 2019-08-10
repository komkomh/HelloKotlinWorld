import java.io.BufferedWriter
import java.io.FileWriter
import java.io.PrintWriter

fun main() {
    println("tsv output start.")

    val n = 20000 // 件数
    writeProjects(n)
    writePeriodLinkages(n)
    writeTagLinkages(n)

    println("tsv output finished.")
}

fun writeProjects(n: Int) {
    val fil = FileWriter("/Users/curp043/Develop/OLC/IFM-ConfigulationManagement/Vagrant/tools/datas/future_maintenance_plan_formulation_projects.csv")
    val pw = PrintWriter(BufferedWriter(fil))
    (1..n).forEach{
        pw.println("${it}\t${it}\t1\t0.0.0\t2021\t1\tENA\t1\tDTM\t2\t2\t28\t38\t1\tサンプルTDL投資${it}\t0\t12345\t0\t0\t0\t0\t1\t1\tRQ-12345\t\t0\t3\t0\t1\t1\tC7006819\t1\t1\tC7006819\t0\t2019-04-17 09:44:34.250\t2019-04-17 09:44:34.250\t\t2019\t\t\n")
    }
    pw.close()
}

fun writePeriodLinkages(n: Int) {
    val fil = FileWriter("/Users/curp043/Develop/OLC/IFM-ConfigulationManagement/Vagrant/tools/datas/future_maintenances_period_linkages.csv")
    val pw = PrintWriter(BufferedWriter(fil))
    (1..n).forEach{
        if (it % 2 == 1) {
            pw.println("${it}\t${it}\t${it + 1}\t2019-04-17 09:44:34.293\t2019-04-17 09:44:34.293\n")
        }
    }
    pw.close()
}

fun writeTagLinkages(n: Int) {
    val fil = FileWriter("/Users/curp043/Develop/OLC/IFM-ConfigulationManagement/Vagrant/tools/datas/future_maintenances_total_tags_linkages.csv")
    val pw = PrintWriter(BufferedWriter(fil))
    (1..n).forEach{
        pw.println("${it}\t${it}\t${it % 3}\tC7006819\tC7006819\t2019-04-17 09:44:34.253\t2019-04-17 09:44:34.253\n")
    }
    pw.close()
}