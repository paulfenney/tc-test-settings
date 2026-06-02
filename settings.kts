import jetbrains.buildServer.configs.kotlin.*
import jetbrains.buildServer.configs.kotlin.buildSteps.exec
import jetbrains.buildServer.configs.kotlin.triggers.vcs
import jetbrains.buildServer.configs.kotlin.vcs.GitVcsRoot

/*
The settings script is an entry point for defining a TeamCity
project hierarchy. The script should contain a single call to the
project() function with a Project instance or an init function as
an argument.

VcsRoots, BuildTypes, Templates, and subprojects can be
registered inside the project using the vcsRoot(), buildType(),
template(), and subProject() methods respectively.

To debug settings scripts in command-line, run the

    mvnDebug org.jetbrains.teamcity:teamcity-configs-maven-plugin:generate

command and attach your debugger to the port 8000.

To debug in IntelliJ Idea, open the 'Maven Projects' tool window (View
-> Tool Windows -> Maven Projects), find the generate task node
(Plugins -> teamcity-configs -> teamcity-configs:generate), the
'Debug' option is available in the context menu for the task.
*/

version = "2026.1"

project {
    description = "Testing ways of configuring TC versioned settings without breaking our protected branch/PR workflow."

    vcsRoot(HttpsGithubComPaulfenneyTcTestMainRefsHeadsMain)

    buildType(Build)
}

object Build : BuildType({
    name = "Build"

    vcs {
        root(HttpsGithubComPaulfenneyTcTestMainRefsHeadsMain)
    }

    steps {
        exec {
            name = "Change to build settings that should not affect main"
            id = "Do_something"
            path = "wc"
            arguments = "-l README.md"
            param("script.content", "cat README.md")
        }
    }

    triggers {
        vcs {
        }
    }
})

object HttpsGithubComPaulfenneyTcTestMainRefsHeadsMain : GitVcsRoot({
    name = "Main Repo"
    url = "git@github.com:paulfenney/tc-test-main.git"
    branch = "refs/heads/main"
    branchSpec = "refs/heads/*"
    authMethod = uploadedKey {
        uploadedKey = "Deploy Key for Main"
    }
})
