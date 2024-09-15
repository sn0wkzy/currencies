dependencies {
    implementation(project(":currencies-common"))

    compileOnly(libs.spigot)

    implementation(libs.mongodb)
    implementation(libs.inventory.framework)
    implementation(libs.configuration)

    compileOnly(libs.lombok)
    annotationProcessor(libs.lombok)
}