def osflag_list = ["unix", "unixmpi", "csm_mio", "unix_pgf", "unixg95"]

folder("Builds")

def ii = 0
osflag_list.each {
  def compiler = it
  def jobname = "$Builds/${compiler}"

  job(jobname) {
    parameter {
      stringParam("Build flags", "")
    }

    scm {
      git {
        remote {
          url("https://git.code.sf.net/p/openmps/v3code")
        }
        branch("master")
      }
    }

    steps {
      shell("python BuildOSMPS.py --os=${} --prefix='.' --clean")
      shell("rm -rf *")
    }
  }
}

job("Builds_RunAll") {
  parameter {
    stringParam("Build flags", "")
  }

  steps {
    osflag_list.each {
      compiler = it

      downstreamParameterized {
        trigger("Builds/${compiler}"){
	  parameters {
	    currentBuild()
	  }
	}
      }
    }
  }
}