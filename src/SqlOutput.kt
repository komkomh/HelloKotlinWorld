import java.io.BufferedWriter
import java.io.FileWriter
import java.io.PrintWriter

fun main() {
    println("sql output start.")

    val begin = 85331 // 開始インデックス
    val end = 185331   // 終了インデックス
    writeProjectSql(begin, end)
    writePeriodLinkageSql(begin, end)
    writeTagLinkageSql(begin, end)

    println("sql output finished.")
}

fun writeProjectSql(begin: Int, end: Int) {
    val fil = FileWriter("/Users/curp043/insert_future_maintenance_plan.sql")
    val pw = PrintWriter(BufferedWriter(fil))
    (begin until end).forEach {
        //        pw.println("${it}\t${it}\t1\t0.0.0\t2021\t1\tENA\t1\tDTM\t2\t2\t28\t38\t1\tサンプルTDL投資${it}\t0\t12345\t0\t0\t0\t0\t1\t1\tRQ-12345\t\t0\t3\t0\t1\t1\tC7006819\t1\t1\tC7006819\t0\t2019-04-17 09:44:34.250\t2019-04-17 09:44:34.250\t\t2019\t\t\n")
        pw.println(
            "INSERT INTO future_maintenance_plan_formulation_projects (" +
                    "future_maintenance_plan_formulation_project_id, history_number, version, term_year, charge_department_id, charge_department_name, charge_department_group_id, charge_department_group_name, park_id, park_area_id, area_complex_id, complex_facility_id, type_id, project_title, project_type, budget_amount, working_time_division, temporary_enclosure_days, closed_days, is_rc, project_cycle_period, project_priority_division, project_request_id, remarks, is_reminder, project_status, is_editing, created_department_id, created_department_group_id, created_employee_number, updated_department_id, updated_department_group_id, updated_employee_number, is_deleted, created_at, updated_at, original_project_id, year_version, determination_employee_number, determination_at) " +
                    "VALUES (${it}, 1, '0.0.0', 2025, 1, 'ENA', 1, 'DTM', 2, 2, 28, 38, 1, 'サンプルTDL投資${it}', 0, 12345, 0, 0, 0, 0, 1, 1, 'RQ-12345', null, 0, 3, 0, 1, 1, 'C7006819', 1, 1, 'C7006819', 0, '2019-04-17 09:44:34.250', '2019-04-17 09:44:34.250', null, 2023, null, null)"
        )
    }
    pw.close()
}

fun writePeriodLinkageSql(begin: Int, end: Int) {
    val fil =
        FileWriter("/Users/curp043/insert_period_linkages.sql")
    val pw = PrintWriter(BufferedWriter(fil))
    (begin until end).forEach {
        if (it % 2 == 1) {
//            pw.println("${it}\t${it}\t${it + 1}\t2019-04-17 09:44:34.293\t2019-04-17 09:44:34.293\n")
            pw.println("INSERT INTO future_maintenances_period_linkages (" +
                    "parent_future_maintenance_plan_formulation_project_id, child_future_maintenance_plan_formulation_project_id, created_at, updated_at) VALUES (" +
                    "${it}, ${it + 1}, '2019-04-17 09:44:34.293', '2019-04-17 09:44:34.293');")
        }
    }
    pw.close()
}

fun writeTagLinkageSql(begin: Int, end: Int) {
    val fil =
        FileWriter("/Users/curp043/insert_tags_linkages.sql")
    val pw = PrintWriter(BufferedWriter(fil))
    (begin until end).forEach {
//        pw.println("${it}\t${it}\t${it % 3}\tC7006819\tC7006819\t2019-04-17 09:44:34.253\t2019-04-17 09:44:34.253\n")
        pw.println("INSERT INTO future_maintenances_total_tags_linkages (" +
                "future_maintenance_plan_formulation_project_id, total_tag_id, created_employee_number, updated_employee_number, created_at, updated_at" +
                ") VALUES (" +
                "$${it}, ${it % 3 + 1}, 'C7006819', 'C7006819', '2019-04-17 09:44:34.253', '2019-04-17 09:44:34.253');")
    }
    pw.close()
}