package corda.contract

import net.corda.core.contracts.ContractState
import net.corda.core.serialization.CordaSerializable

@CordaSerializable
interface Fruit  :ContractState{
    fun getColor():List<String>
}