import axios from "axios";

export const getCustomer = async () =>{
    try {
        return await axios.get(`${import.meta.env.VITE_API_BASE_URL}/api/v1/customers`)
    }catch (e){
        throw e;
    }
}

export const saveCustomer = async (customer) =>{
    try {
        console.log(customer);
        return await axios.post(`${import.meta.env.VITE_API_BASE_URL}/api/v1/customers`,
            customer)
    }catch (e){
        throw e;
    }
}

export const deleteCustomer = async (id) =>{
    try {
        return await axios.delete(`${import.meta.env.VITE_API_BASE_URL}/api/v1/customers/${id}`)
    }catch (e){
        throw e;
    }
}

export const updateCustomer = async (id,update) =>{
    try {
        console.log(update);
        console.log(id);
        return await axios.put(`${import.meta.env.VITE_API_BASE_URL}/api/v1/customers/${id}`,
            update)
    }catch (e){
        throw e;
    }
}