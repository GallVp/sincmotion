#!/usr/bin/env bash

declared_version=$(grep "version = " ./sincmotionmp/build.gradle.kts | sed 's/version = //' | sed 's/"//g')
# citation_version=$(grep "version: " ./CITATION.cff | tail -1 | sed 's/version: //')
# readme_version=$(grep 'implementation("io.github.gallvp:sincmotionmp:' ./README.md | sed 's/implementation("io.github.gallvp:sincmotionmp://' | sed 's/")//')
changelog_version=$(head -n 3 CHANGELOG.md | sed -n 's/.*Version \(.*\) .*/\1/p')

# if [[ "$citation_version" != "$declared_version" ]];then
#     echo "citation_version ($citation_version) is not the same as declared_version ($declared_version) version"
#     exit 1
# fi

# if [[ "$readme_version" != "$declared_version" ]];then
#     echo "readme_version ($readme_version) is not the same as declared_version ($declared_version) version"
#     exit 1
# fi

if [[ "$changelog_version" != "$declared_version" ]];then
    echo "changelog_version ($changelog_version) is not the same as declared_version ($declared_version) version"
    exit 1
fi
