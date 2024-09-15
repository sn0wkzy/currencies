dependencies {
    compileOnly(libs.spigot)

    compileOnly(libs.annotations)
    compileOnly(libs.configuration)

    compileOnly(libs.mongodb)
    compileOnly(libs.luckperms.api)

    compileOnly(libs.lombok)
    annotationProcessor(libs.lombok)

    implementation(libs.caffeine)
}