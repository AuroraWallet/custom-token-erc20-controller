package com.aurora.aurorawallet.utils.infoContract

import android.util.Log
import org.web3j.abi.FunctionEncoder
import org.web3j.abi.FunctionReturnDecoder
import org.web3j.abi.TypeReference
import org.web3j.abi.datatypes.Address
import org.web3j.abi.datatypes.Function
import org.web3j.abi.datatypes.Type
import org.web3j.abi.datatypes.Utf8String
import org.web3j.abi.datatypes.generated.Uint256
import org.web3j.abi.datatypes.generated.Uint8
import org.web3j.protocol.Web3j
import org.web3j.protocol.core.DefaultBlockParameterName
import org.web3j.protocol.core.methods.request.Transaction
import org.web3j.protocol.core.methods.response.EthCall
import java.io.IOException
import java.math.BigDecimal
import java.math.BigInteger
import java.util.concurrent.ExecutionException

class InfoContractAddressEthLike {
    private val emptyAddress = "0x0000000000000000000000000000000000000000"

    fun getTokenBalance(
        web3j: Web3j,
        fromAddress: String?,
        contractAddress: String?
    ): BigInteger? {
        val methodName = "balanceOf"
        val inputParameters: MutableList<Type<*>> = ArrayList()
        val outputParameters: MutableList<TypeReference<*>> = ArrayList()
        val address = Address(fromAddress)
        inputParameters.add(address)
        val typeReference: TypeReference<Uint256> = object : TypeReference<Uint256>() {}
        outputParameters.add(typeReference)
        val function = Function(methodName, inputParameters, outputParameters)
        val data = FunctionEncoder.encode(function)
        val transaction = Transaction.createEthCallTransaction(fromAddress, contractAddress, data)
        val ethCall: EthCall
        var balanceValue = BigInteger.ZERO
        try {
            ethCall = web3j.ethCall(transaction, DefaultBlockParameterName.LATEST).send()
            val results =
                FunctionReturnDecoder.decode(ethCall.getValue(), function.outputParameters)
            if (results != null && results.size > 0) {
                balanceValue = results[0].value as BigInteger
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return balanceValue
    }

    fun getTokenBalance(
        web3j: Web3j?,
        fromAddress: String?,
        contractAddress: String?,
        tokenDecimal: Int
    ): Double {
        val bal = getTokenBalance(web3j!!, fromAddress, contractAddress)
        var balance = BigDecimal(bal)
        balance = balance.divide(BigDecimal.TEN.pow(tokenDecimal))
        return balance.toDouble()
    }

    fun getTokenName(web3j: Web3j, contractAddress: String?): String? {
        val methodName = "name"
        var name: String? = null
        val fromAddr = emptyAddress
        val inputParameters: List<Type<*>> = ArrayList()
        val outputParameters: MutableList<TypeReference<*>> = ArrayList()
        val typeReference: TypeReference<Utf8String> = object : TypeReference<Utf8String>() {}
        outputParameters.add(typeReference)
        val function = Function(methodName, inputParameters, outputParameters)
        val data = FunctionEncoder.encode(function)
        val transaction = Transaction.createEthCallTransaction(fromAddr, contractAddress, data)
        val ethCall: EthCall
        try {
            ethCall = web3j.ethCall(transaction, DefaultBlockParameterName.LATEST).sendAsync().get()
            val results =
                FunctionReturnDecoder.decode(ethCall.getValue(), function.outputParameters)
            name = results[0].value.toString()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        } catch (e: ExecutionException) {
            e.printStackTrace()
        }
        return name
    }

    fun getTokenSymbol(web3j: Web3j, contractAddress: String?): String? {
        val methodName = "symbol"
        var symbol: String? = null
        val fromAddr = emptyAddress
        val inputParameters: List<Type<*>> = ArrayList()
        val outputParameters: MutableList<TypeReference<*>> = ArrayList()
        val typeReference: TypeReference<Utf8String> = object : TypeReference<Utf8String>() {}
        outputParameters.add(typeReference)
        val function = Function(methodName, inputParameters, outputParameters)
        val data = FunctionEncoder.encode(function)
        val transaction = Transaction.createEthCallTransaction(fromAddr, contractAddress, data)
        val ethCall: EthCall
        try {
            ethCall = web3j.ethCall(transaction, DefaultBlockParameterName.LATEST).sendAsync().get()
            val results =
                FunctionReturnDecoder.decode(ethCall.getValue(), function.outputParameters)
            symbol = results[0].value.toString()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        } catch (e: ExecutionException) {
            e.printStackTrace()
        }
        return symbol
    }

    fun getTokenDecimals(web3j: Web3j, contractAddress: String?): Int {
        val methodName = "decimals"
        val fromAddr = emptyAddress
        var decimal = 18
        val inputParameters: List<Type<*>> = ArrayList()
        val outputParameters: MutableList<TypeReference<*>> = ArrayList()
        val typeReference: TypeReference<Uint8> = object : TypeReference<Uint8>() {}
        outputParameters.add(typeReference)
        val function = Function(methodName, inputParameters, outputParameters)
        val data = FunctionEncoder.encode(function)
        val transaction = Transaction.createEthCallTransaction(fromAddr, contractAddress, data)
        val ethCall: EthCall
        try {
            ethCall = web3j.ethCall(transaction, DefaultBlockParameterName.LATEST).sendAsync().get()
            val results =
                FunctionReturnDecoder.decode(ethCall.getValue(), function.outputParameters)
            Log.i("getTokenDecimals", "getTokenDecimals: $results")
            Log.i("getTokenDecimals", "getTokenDecimals size: ${results.size}")
            if (results?.size != null) {
                decimal = results?.get(0)?.value.toString().toInt()
            } else {
                decimal = 18
            }
        } catch (e: InterruptedException) {
            e.printStackTrace()
        } catch (e: ExecutionException) {
            e.printStackTrace()
        }
        return if (decimal != null) {
            decimal
        } else {
            18
        }
    }


    fun getTokenTotalSupply(web3j: Web3j, contractAddress: String?): BigInteger? {
        val methodName = "totalSupply"
        val fromAddr = emptyAddress
        var totalSupply = BigInteger.ZERO
        val inputParameters: List<Type<*>> = ArrayList()
        val outputParameters: MutableList<TypeReference<*>> = ArrayList()
        val typeReference: TypeReference<Uint256> = object : TypeReference<Uint256>() {}
        outputParameters.add(typeReference)
        val function = Function(methodName, inputParameters, outputParameters)
        val data = FunctionEncoder.encode(function)
        val transaction = Transaction.createEthCallTransaction(fromAddr, contractAddress, data)
        val ethCall: EthCall
        try {
            ethCall = web3j.ethCall(transaction, DefaultBlockParameterName.LATEST).sendAsync().get()
            val results =
                FunctionReturnDecoder.decode(ethCall.getValue(), function.outputParameters)
            totalSupply = results[0].value as BigInteger
        } catch (e: InterruptedException) {
            e.printStackTrace()
        } catch (e: ExecutionException) {
            e.printStackTrace()
        }
        return totalSupply
    }
}