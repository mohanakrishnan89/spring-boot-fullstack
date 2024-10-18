import {Wrap,
        WrapItem,
        Spinner,
        Text} from "@chakra-ui/react";
import SidebarWithHeader from "./components/shared/SideBar.jsx"
import CardWithImage from "./components/Card.jsx";
import {useEffect,useState} from "react";
import {getCustomer} from "./services/client.js";
import CreateCustomerDrawer from "./components/CreateCustomerDrawer.jsx";
import {errorNotification} from "./services/notification.js";

const App = () =>{
    const[isLoading, setIsLoading] = useState(false);
    const[customers,setCustomers] = useState([]);
    const[err, setError] = useState("");

    const fetchCustomers = () =>{
        setIsLoading(true);
        getCustomer().then(res =>{
            setCustomers(res.data);
        }).catch(function (err) {
            setError(err.response.data.message)
            errorNotification(
                err.code,
                err.response.data.message
            )
        }).finally(() =>{
            setIsLoading(false);
        })
    }

    useEffect(() => {
        fetchCustomers();
    }, []);



    if(isLoading)
        return (
            <SidebarWithHeader>
                <Spinner
                    thickness='4px'
                    speed='0.65s'
                    emptyColor='gray.200'
                    color='blue.500'
                    size='xl'
                />
            </SidebarWithHeader>
        )

    if(err)
        return (
            <SidebarWithHeader>
                <CreateCustomerDrawer fetchCustomers = {fetchCustomers}/>
                <Text mt={"5px"}>OOPS!! There is an Error!</Text>
            </SidebarWithHeader>
        )

    if(customers.length <= 0)
        return (
            <SidebarWithHeader>
                <CreateCustomerDrawer fetchCustomers = {fetchCustomers}/>
               <Text mt={"5px"}>No Customers found</Text>
            </SidebarWithHeader>
        )
    return(
        <SidebarWithHeader>
            <CreateCustomerDrawer fetchCustomers = {fetchCustomers}/>
            <Wrap justify={"center"} spacing={"30px"}>
            {customers.map((customer,index) =>(
                <WrapItem key={index}>
                    <CardWithImage
                        {... customer}
                        imageNumber={customer.id}
                        fetchCustomers = {fetchCustomers}/>
                </WrapItem>
            ))}
            </Wrap>
        </SidebarWithHeader>
    )
}

export default App