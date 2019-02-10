def seeder_list = ["seeder_examples"]

seeder_list.each {
  def seeder_name = it
  
  job(seeder_name) {
    steps{
      dsl {
        external("${seeder_name}.groovy")
      }
    }
  }
}

job("seeder_seeders") {
  
  scm {
    git {
      remote {
        url("https://github.com/djaschke1/CIOSMPS.git")
      }
      branch("master")
    }
  }
  
  seeder_list.each {
    def sn = it
    
    steps {
      shell("cp seeders/${sn}.groovy ../${sn}/.")
    }
    
    publishers {
      downstream("${sn}", "SUCCESS")
    }
  }
}