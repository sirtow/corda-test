package corda.contract

import net.corda.core.contracts.Contract
import net.corda.core.serialization.CordaSerializable
import net.corda.core.transactions.LedgerTransaction

@CordaSerializable
class LemonContract :FruitContract(){
//    override fun verify(tx: LedgerTransaction) {
//        println("============ LemonContract :: verify  !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!")
//    }

    override fun childVerify(tx: LedgerTransaction) {
        println("============ LemonContract :: verify  !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!")
        println("Lemon weight is :${tx.outputsOfType<Lemon>()[0].weight}")

    }
}