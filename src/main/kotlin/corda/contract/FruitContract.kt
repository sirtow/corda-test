package corda.contract

import net.corda.core.contracts.CommandData
import net.corda.core.contracts.Contract
import net.corda.core.contracts.TypeOnlyCommandData
import net.corda.core.serialization.CordaSerializable
import net.corda.core.transactions.LedgerTransaction

@CordaSerializable
abstract class FruitContract: Contract {
    interface Commands : CommandData {
        class addFruit : TypeOnlyCommandData(), Commands

    }
    override fun verify(tx: LedgerTransaction) {
        println("============ FruitContract :: verify  !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!")
        println("Fruit color is : ${tx.outputsOfType<Fruit>()[0].getColor()}")
        childVerify(tx)
    }

    open fun childVerify(tx: LedgerTransaction) {}

}