Pod::Spec.new do |spec|
    spec.name                     = 'SincMotion'
    spec.version                  = '0.3'
    spec.homepage                 = 'https://github.com/GallVp/sincmotion'
    spec.source                   = { :http=> ''}
    spec.authors                  = 'Usman Rashid'
    spec.license                  = ''
    spec.summary                  = 'SincMotion: A Kotlin multi-platform implementation of gait and balance algorithms'
    spec.vendored_frameworks      = 'build/cocoapods/framework/SincMotion.framework'
    spec.libraries                = 'c++'
    spec.ios.deployment_target = '13.0'


    if !Dir.exist?('build/cocoapods/framework/SincMotion.framework') || Dir.empty?('build/cocoapods/framework/SincMotion.framework')
        raise "

        Kotlin framework 'SincMotion' doesn't exist yet, so a proper Xcode project can't be generated.
        'pod install' should be executed after running ':generateDummyFramework' Gradle task:

            ./gradlew :sincmotionmp:generateDummyFramework

        Alternatively, proper pod installation is performed during Gradle sync in the IDE (if Podfile location is set)"
    end

    spec.pod_target_xcconfig = {
        'KOTLIN_PROJECT_PATH' => ':sincmotionmp',
        'PRODUCT_MODULE_NAME' => 'SincMotion',
    }

    spec.script_phases = [
        {
            :name => 'Build SincMotion',
            :execution_position => :before_compile,
            :shell_path => '/bin/sh',
            :script => <<-SCRIPT
                if [ "YES" = "$OVERRIDE_KOTLIN_BUILD_IDE_SUPPORTED" ]; then
                  echo "Skipping Gradle build task invocation due to OVERRIDE_KOTLIN_BUILD_IDE_SUPPORTED environment variable set to \"YES\""
                  exit 0
                fi
                set -ev
                REPO_ROOT="$PODS_TARGET_SRCROOT"
                "$REPO_ROOT/../gradlew" -p "$REPO_ROOT" $KOTLIN_PROJECT_PATH:syncFramework \
                    -Pkotlin.native.cocoapods.platform=$PLATFORM_NAME \
                    -Pkotlin.native.cocoapods.archs="$ARCHS" \
                    -Pkotlin.native.cocoapods.configuration="$CONFIGURATION"
            SCRIPT
        }
    ]

end
