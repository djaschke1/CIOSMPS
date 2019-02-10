def example_list = ["01_IsingStatics", "02_BoseHubbardStatics"]

folder("Examples")

example_list.each {
  def example_name = it
  def filename = "${example_name}.py"
  def jobname = "Examples/${example_name}"
  
  job(jobname) {
    parameters {
      choiceParam("PythonVersion", ["python", "python3"])
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
      shell("\$PythonVersion BuildOSMPS.py --os=unix0 --prefix='.'")
      shell("cp Examples/${filename} .")
      shell("\$PythonVersion ${filename} --PostProcess=F")
      shell("rm -rf OUTPUTS_* TMP_* ${filename}")
    }
  }
}

job("Examples_RunAll") {
  parameters {
      choiceParam("PythonVersion", ["python", "python3"])
    }

  /*publishers {
    example_list.each {
      downstream("Examples/${it}", "SUCCESS")
    }
  }*/

  steps {
    example_list.each {
      def example_name = it

      downstreamParameterized {
        trigger("Examples/${example_name}") {
	  parameters {
	    currentBuild()
	  }
	}
      }
    } 
  }
}