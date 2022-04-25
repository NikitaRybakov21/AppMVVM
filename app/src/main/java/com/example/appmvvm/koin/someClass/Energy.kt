package koin.someClass

class Energy(private val string : String = "string2") : IEnergy{

    override fun out(){
        System.out.println(string)
    }

}
