package koin.someClass

class Energy2(private val string : String = "string2") : IEnergy {

    override fun out(){
        System.out.println(string + "888")
    }

}